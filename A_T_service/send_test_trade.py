import sys
import subprocess
import time

# Auto-install kafka-python if not present
try:
    from kafka import KafkaProducer
except ImportError:
    print("Kafka library not found. Installing 'kafka-python-ng' dynamically...")
    subprocess.check_call([sys.executable, "-m", "pip", "install", "kafka-python-ng"])
    from kafka import KafkaProducer

def send_message():
    bootstrap_servers = 'localhost:9092'
    topic = 'trade-topic'
    
    print(f"Connecting to Kafka broker at {bootstrap_servers}...")
    try:
        producer = KafkaProducer(
            bootstrap_servers=bootstrap_servers,
            key_serializer=lambda k: k.encode('utf-8') if k else None,
            value_serializer=lambda v: v.encode('utf-8')
        )
    except Exception as e:
        print(f"\n[ERROR] Could not connect to Kafka broker. Ensure Kafka is running on {bootstrap_servers}.")
        print("You can start it using: docker compose up -d")
        print(f"Details: {e}")
        sys.exit(1)

    xml_message = """<trade>
    <tradeId>TR-999</tradeId>
    <time>2026-05-28T08:50:00Z</time>
    <amount>750000.00</amount>
    <accountNumber>ACC1001</accountNumber>
</trade>"""

    print(f"Publishing XML Trade message to topic '{topic}':")
    print(xml_message)
    
    future = producer.send(topic, value=xml_message, key="TR-999")
    
    try:
        record_metadata = future.get(timeout=10)
        print("\n[SUCCESS] Message successfully published!")
        print(f"Topic: {record_metadata.topic}")
        print(f"Partition: {record_metadata.partition}")
        print(f"Offset: {record_metadata.offset}")
    except Exception as e:
        print(f"\n[ERROR] Failed to send message: {e}")
    finally:
        producer.close()

if __name__ == "__main__":
    send_message()
