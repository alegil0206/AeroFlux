from pymongo import MongoClient
import os

MONGO_URI = os.getenv("MONGODB_URI")
DATABASE_NAME = os.getenv("MONGODB_DATABASE")
# DRONE_COLLECTION_NAME = os.getenv("DRONE_COLLECTION")
OPERATION_CATEGORY_COLLECTION_NAME = os.getenv("OPERATION_CATEGORY_COLLECTION", "operation")

OPERATION_CATEGORIES = [
    {"name": "CERTIFIED", "description": "Low priority category for specific operations."},
    {"name": "SPECIFIC", "description": "High priority category for certified operations."},
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

    operation_category_collection = db[OPERATION_CATEGORY_COLLECTION_NAME]

    print("Initializing operation category...")
    initialize_collection(operation_category_collection, OPERATION_CATEGORIES, "name")

    print("Initialization completed.")
    client.close()

if __name__ == "__main__":
    main()