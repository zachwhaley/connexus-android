import cgi
import datetime
import json
import urllib

from google.appengine.api import users
from google.appengine.api.images import get_serving_url
from google.appengine.ext import db
from google.appengine.ext import blobstore
from google.appengine.ext.webapp import blobstore_handlers

import webapp2

class Stream(db.Expando):
    name = db.StringProperty()
    tags = db.StringProperty()
    cover_url = db.StringProperty()
    followers = db.StringListProperty()
    date = db.DateTimeProperty(auto_now_add=True)

class Image(db.Model):
    image_url = db.StringProperty()
    latitude = db.FloatProperty()
    longitude = db.FloatProperty()
    date = db.DateTimeProperty(auto_now_add=True)

class AddStream(webapp2.RequestHandler):
    def post(self):
        stream = Stream()
        stream.name = self.request.get('name')
        stream.tags = self.request.get('tags')
        stream.cover_url = self.request.get('cover_url')
        stream.followers = []
        stream.put()

class GetUploadUrl(webapp2.RequestHandler):
    def get(self):
        upload_url = blobstore.create_upload_url('/api/upload/handler')
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.out.write(upload_url)

class UploadImage(webapp2.RequestHandler):
    def post(self):
        upload_url = blobstore.create_upload_url('/api/upload/handler')
        self.response.headers['Content-Type'] = 'text/plain'
        self.response.out.write(upload_url)

class UploadHandler(blobstore_handlers.BlobstoreUploadHandler):
    def post(self):
        upload_files = self.get_uploads('file')
        blob_info = upload_files[0]
        key = blob_info.key()

        serving_url = get_serving_url(key)
        stream_id = self.request.get('stream')
        latitude = self.request.get('latitude')
        longitude = self.request.get('longitude')
        stream = Stream.get_by_id(long(stream_id))

        image = Image(parent=stream)
        image.latitude = float(latitude)
        image.longitude = float(longitude)
        image.image_url = serving_url
        image.put()

class Subscribe(webapp2.RequestHandler):
    def post(self):
        stream_id = self.request.get('stream')
        email = self.request.get('email')
        stream = Stream.get_by_id(long(stream_id))
        stream.followers.append(email)
        stream.put()

class AllStreams(webapp2.RequestHandler):
    def get(self):
        query = Stream.all()
        query.order('-date')
        streams = [db.to_dict(stream) for stream in query.run()]
        self.response.headers['Content-Type'] = 'application/json'
        self.response.out.write(json.dumps(streams, cls=DateSkipper))

class MyStreams(webapp2.RequestHandler):
    def get(self):
        query = Stream.all()
        query.order('-date')
        email = self.request.get('email')
        streams = [db.to_dict(stream) for stream in query.run() if email in stream.followers]
        self.response.headers['Content-Type'] = 'application/json'
        self.response.out.write(json.dumps(streams, cls=DateSkipper))

class StreamImages(webapp2.RequestHandler):
    def get(self):
        stream_id = self.request.get('stream')
        stream = Stream.get_by_id(long(stream_id))
        query = Image.all()
        query.ancestor(stream)
        images = [db.to_dict(image) for image in query.run()]
        self.response.headers['Content-Type'] = 'application/json'
        self.response.out.write(json.dumps(images, cls=DateSkipper))

class DateSkipper(json.JSONEncoder):
    def default(self, obj):
        if isinstance(obj, datetime.datetime):
            return
        return json.JSONEncoder.default(self, obj) 

application = webapp2.WSGIApplication([
    ('/', MainHandler),
    ('/api/addstream', AddStream),
    ('/api/allstreams', AllStreams),
    ('/api/mystreams', MyStreams),
    ('/api/images', StreamImages),
    ('/api/subscribe', Subscribe),
    ('/api/upload/geturl', UploadImage),
    ('/api/upload/handler', UploadHandler),
], debug=True)
