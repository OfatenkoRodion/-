Some help data

docker pull docker.elastic.co/elasticsearch/elasticsearch:6.8.16
docker run -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" --name local_elastic docker.elastic.co/elasticsearch/elasticsearch:6.8.16
http://localhost:9200/_cluster/health?level=shards&pretty


docker pull redis
docker run -p 6379:6379 --name redis -d redis

create in-x PUT localhost:9200/click
{
    "settings": {
        "index.number_of_shards" : 1,
        "index.number_of_replicas": 0
    },
    "mappings": {
        "demo": {
            "properties": {
                "item_id": {"type": "text"},
                "name": {"type": "text"},
                "locale": {"type": "text"},
                "click": {"type": "integer"},
                "purchase": {"type": "integer"}
            }
        }
    }
}

clean in-x DELETE localhost:9200/click

add data no auto in-x PUT localhost:9200/click/demo/1
{
    "item_id":"399",
    "name":"Single",
    "locale":"tr_TR",
    "click":122,
    "purchase":904
}

add data auto in-x POST localhost:9200/click/demo
{
    "item_id":"399",
    "name":"Single",
    "locale":"tr_TR",
    "click":122,
    "purchase":904
}

view data GET localhost:9200/click/_search?pretty
