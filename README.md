#Imperials

A data repository and dashboard for Yammer Metrics.

## Requirements

* MongoDB

## Components

* `client` - An HTTP client that periodically pulls Yammer metrics data from services and stores it in MongoDB.

* `dashboard` - A web UI for displaying real-time and historical metrics data

* `test/mock-service` - A simple example service that you can use for testing
