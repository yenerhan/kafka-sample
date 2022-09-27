# kafka-sample

with docker-compose-3b1z.yml file, we can run 1 zookeeper, 3 kafka broker and kafdrop.

and then on terminal in .yml folder:

we can execute yml file.
docker-compose -f docker-compose.yml up -d


docker ps:You can see the running docker containers.

After all of them. we can invoke producer service with this request by posman

Method:localhost:8080/api/v1/car/saveCar

request:
{
    "car":{
        "id":4,
        "name":"Test4"
    }
}

How to see topic:
Kafdrop address: http://localhost:9000/

When add topic, consumer service listeners catch this topic. Then save the database.
H2 Database address: http://localhost:8088/h2/
