from flask import Flask
from flask_restful import Api
import endpoints


class Pages:
    @staticmethod
    def index():
        return "<h1>Assembee Server</h1>", 200


class Server:
    def __init__(self):
        self.app = Flask("Assembee")
        self.api = Api(self.app)
        self.base_url = "/"

    def start(self, PORT):
        self.app.add_url_rule("/", "index", Pages.index)

        resources = [(endpoints.User, f"{self.base_url}user/<string:username>")]
        for endpoint, uri in resources:
            self.api.add_resource(endpoint, uri)

        self.app.run(debug=True, port=PORT)
        

if __name__ == "__main__":
    server = Server()
    server.start(6969)
    app = server.app # WSGI entry point
