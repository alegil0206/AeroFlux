from pymongo import MongoClient
import os

MONGO_URI = os.getenv("MONGODB_URI")
DATABASE_NAME = os.getenv("MONGODB_DATABASE")
# GEOZONE_COLLECTION_NAME = os.getenv("GEOZONE_COLLECTION", "geozone")
TYPE_COLLECTION_NAME = os.getenv("TYPE_COLLECTION", "type")
CATEGORY_COLLECTION_NAME = os.getenv("CATEGORY_COLLECTION", "category")
STATUS_COLLECTION_NAME = os.getenv("STATUS_COLLECTION", "status")
ALTITUDE_COLLECTION_NAME = os.getenv("ALTITUDE_COLLECTION", "altitude")

ALTITUDES = [
    {"name": "L0", "value": 0,},
    {"name": "L1", "value": 25},
    {"name": "L2", "value": 45},
    {"name": "L3", "value": 60},
    {"name": "L4", "value": 120},
]

CATEGORIES = [
    {"name": "RESTRICTED", "description": "Geozone which requires authorization to be accessed."},
    {"name": "EXCLUDED", "description": "Geozone which is not allowed to be accessed."},
]

STATUSES = [
    {"name": "ACTIVE", "description": "Geozone which is active."},
    {"name": "INACTIVE", "description": "Geozone which is not active and can be normally accessed."},
]

TYPES = [
    {"name": "CIRCULAR", "description": "Cylindrical geozone with a center (geographic coordinates), a radius (meters), and an altitude (meters)."},
    {"name": "POLYGONAL", "description": "Polygonal geozone within a list of geographic coordinates (at least three) and an altitude (meters)."},
]

def initialize_collection(collection, data, unique_field):
    """
    Inserts data into the collection only if the item does not already exist.
    :param collection: The MongoDB collection.
    :param data: The list of documents to insert.
    :param unique_field: The field used to check uniqueness.
    """
    for item in data:
        if not collection.find_one({unique_field: item[unique_field]}):
            collection.insert_one(item)
            print(f"Inserted: {item}")
        else:
            print(f"Already exists: {item}")

def main():
    print("Connecting to MongoDB with URI: ", MONGO_URI)
    client = MongoClient(MONGO_URI)
    db = client[DATABASE_NAME]

    altitude_collection = db[ALTITUDE_COLLECTION_NAME]
    category_collection = db[CATEGORY_COLLECTION_NAME]
    status_collection = db[STATUS_COLLECTION_NAME]
    type_collection = db[TYPE_COLLECTION_NAME]

    print("Initializing altitudes...")
    initialize_collection(altitude_collection, ALTITUDES, "name")

    print("Initializing categories...")
    initialize_collection(category_collection, CATEGORIES, "name")

    print("Initializing statuses...")
    initialize_collection(status_collection, STATUSES, "name")

    print("Initializing types...")
    initialize_collection(type_collection, TYPES, "name")

    print("Initialization completed.")
    client.close()

if __name__ == "__main__":
    main()