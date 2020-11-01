from flask import Flask
from flask_restful import Api


class Pages:
    @staticmethod
    def index():
        return "<h1>Assembee Server</h1>", 200


class Server:
    def __init__(self):
        self.app = Flask("Assembee")
        self.api = Api(self.app)
        self.base_url = "/"

    def start(self, port: int, debug: bool = True):
        try:
            import lib.endpoints as endpoints
            self.app.add_url_rule("/", "index", Pages.index)

            resources = [(endpoints.User, f"{self.base_url}user/<string:username>")]
            for endpoint, uri in resources:
                self.api.add_resource(endpoint, uri)
        except Exception as error:
            print(error)
            raise
        else:
            self.app.run(debug=debug, port=port)
        

if __name__ == "__main__":
    server = Server()
    app = server.app  # Production WSGI server entry point
    server.start(port=6969)

