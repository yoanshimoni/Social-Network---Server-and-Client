CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -pthread -Iinclude
LDFLAGS:=-lboost_system -pthread

all: EchoClient
	g++ -o bin/BGSclient bin/connectionHandler.o bin/echoClient.o bin/KeyboardAndSendTask.o bin/GetFromServerTask.o $(LDFLAGS)

EchoClient: bin/connectionHandler.o bin/echoClient.o bin/KeyboardAndSendTask.o bin/GetFromServerTask.o
	
bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/echoClient.o: src/echoClient.cpp
	g++ $(CFLAGS) -o bin/echoClient.o src/echoClient.cpp

bin/KeyboardAndSendTask.o: src/KeyboardAndSendTask.cpp
	g++ $(CFLAGS) -o bin/KeyboardAndSendTask.o src/KeyboardAndSendTask.cpp

bin/GetFromServerTask.o: src/GetFromServerTask.cpp
	g++ $(CFLAGS) -o bin/GetFromServerTask.o src/GetFromServerTask.cpp
	
.PHONY: clean
clean:
	rm -f bin/*
