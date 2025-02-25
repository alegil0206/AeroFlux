FROM python:3.11-slim

WORKDIR /app
COPY mongo_init.py /app/

RUN pip install --upgrade pip
RUN pip install pymongo

CMD ["python", "mongo_init.py"]
