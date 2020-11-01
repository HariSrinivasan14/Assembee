from flask import request
from flask_restful import Resource
from google.cloud import firestore

from lib.utils import unpack_document
from lib.database import Database
db = Database()


class User(Resource):
    def get(self, username: str):
        data = {}
        data["username"] = username
        return data, 200


class Project(Resource):
    def get(self, project_id: str):
        data = {}
        data["project_id"] = project_id
        doc = db.document(f"projects/{project_id}").get()
        unpack_document(doc, data)
        return data, 200

    def post(self):
        data = request.json
        return data, 200


class Projects(Resource):
    def get(self, user_id: str):
        data = {}
        data["user_id"] = user_id
        data["projects"] = []
        docs = db.collection("projects").where("owner", "==", db.document(f"users/{user_id}")).stream()
        for doc in docs:
            project = {"project_id": doc.id}
            unpack_document(doc, project)
            data["projects"].append(project)
        return data, 200
