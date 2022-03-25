import fr.polytech.grpc.simple.OpGrpc;
import fr.polytech.grpc.simple.Service.*;

import io.grpc.stub.StreamObserver;



public class MyService extends OpGrpc.OpImplBase {

    public MyService() {
    }

    @Override
    public void calculate(Input input, StreamObserver<Result> responseObserver) {
        int int1 = input.getInt1();
        int int2 = input.getInt2();
        Operation op =  input.getOperation();

        System.out.println("MyService " + this + "  " + int1 + op + int2);

        long res = Long.MAX_VALUE;

        switch (op){
            case ADD -> res = int1 + int2;
            case SUB -> res = int1 - int2;
            case MUL -> res = int1 * int2;
            case DIV -> res = int1 / int2;
            case UNRECOGNIZED -> System.out.println("Operation Unrecognized!");
        }

        Result reply = Result.newBuilder()
                .setResult(res)
                .build();
        responseObserver.onNext(reply);
        responseObserver.onCompleted();
    }

}
