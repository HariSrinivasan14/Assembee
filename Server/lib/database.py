import firebase_admin as firebase
from firebase_admin import credentials, firestore


class Singleton:
    def __init__(self, cls):
        self._cls = cls

    def instance(self):
        try:
            return self._instance
        except AttributeError:
            self._instance = self._cls()
            return self._instance

    def __call__(self):
        return self.instance().get()

    def __instancecheck__(self, instance):
        return isinstance(instance, self._cls)


@Singleton
class Database:
    def __init__(self):
        creds = "config/service_account_creds.json"
        cert = credentials.Certificate(creds)
        firebase.initialize_app(cert)
        self._db = firestore.client()

    def get(self):
        return self._db