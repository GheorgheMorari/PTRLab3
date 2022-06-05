FROM python:3.9
WORKDIR /opt

COPY requirements.txt .
COPY producer.py .
RUN pip install -r requirements.txt

#Run python producer.py