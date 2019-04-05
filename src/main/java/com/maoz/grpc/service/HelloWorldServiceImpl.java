package com.maoz.grpc.service;

import org.lognet.springboot.grpc.GRpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maoz.grpc.Greeting;
import com.maoz.grpc.HelloWorldServiceGrpc.HelloWorldServiceImplBase;
import com.maoz.grpc.Person;

import io.grpc.stub.StreamObserver;

@GRpcService
public class HelloWorldServiceImpl extends HelloWorldServiceImplBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(HelloWorldServiceImpl.class);

	@Override
	public void sayHello(Person request, StreamObserver<Greeting> responseObserver) {
		LOGGER.info("server received {}", request);

		String message = "Hello " + request.getFirstName() + " " + request.getLastName() + "!";
		Greeting greeting = Greeting.newBuilder().setMessage(message).build();
		LOGGER.info("server responded {}", greeting);

		responseObserver.onNext(greeting);
		responseObserver.onCompleted();
	}

	@Override
	public void lotsOfReplies(Person request, StreamObserver<Greeting> responseObserver) {
		LOGGER.info("server received {}", request);

		for (int i = 0; i < 10; i++) {
			String message = "Hello " + i + " : " + request.getFirstName() + " " + request.getLastName() + "!";
			Greeting greeting = Greeting.newBuilder().setMessage(message).build();
			responseObserver.onNext(greeting);
			LOGGER.info("server responded {}", greeting);
		}

		responseObserver.onCompleted();
	}

	@Override
	public StreamObserver<Person> lotsOfGreetings(StreamObserver<Greeting> responseObserver) {
		// TODO Auto-generated method stub
		return new StreamObserver<Person>() {

			String names;

			@Override
			public void onNext(Person value) {
				LOGGER.info("server received {}", value.getFirstName());
				names += value.getFirstName()+",";
			}

			@Override
			public void onError(Throwable t) {
				LOGGER.info("server err {}", t.getMessage());

			}

			@Override
			public void onCompleted() {
				responseObserver.onNext(Greeting.newBuilder().setMessage(names).build());
				responseObserver.onCompleted();
			}
		};
	}

	@Override
	public StreamObserver<Person> bidiHello(StreamObserver<Greeting> responseObserver) {
		// TODO Auto-generated method stub
		return new StreamObserver<Person>() {

			@Override
			public void onNext(Person value) {
				LOGGER.info("server received {}", value.getFirstName());
				responseObserver.onNext(Greeting.newBuilder().setMessage("Hello "+value.getFirstName()+" "+value.getLastName()).build());
			}

			@Override
			public void onError(Throwable t) {
				// TODO Auto-generated method stub
				LOGGER.info("server err {}", t.getMessage());
			}

			@Override
			public void onCompleted() {
				// TODO Auto-generated method stub
				responseObserver.onCompleted();
			}
			
		};
	}

}
