import fr.polytech.grpc.simple.OpGrpc;
import fr.polytech.grpc.simple.OpGrpc.*;
import fr.polytech.grpc.simple.Service;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class Client {

    private ManagedChannel channel;
    protected OpBlockingStub blockingStub;

    public Client(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        initStubs(channel);
    }

    protected void initStubs(ManagedChannel channel) {
        blockingStub = OpGrpc.newBlockingStub(channel);
    }

    public long calc(int int1, int int2, Service.Operation operation) throws IllegalArgumentException{
        if (operation == Service.Operation.DIV && int2 == 0)
            throw new IllegalArgumentException("Division By Zero Error!");
        Service.Input request = Service.Input.newBuilder().setInt1(int1).setInt2(int2).setOperation(operation).build();
        Service.Result reply = blockingStub.calculate(request);
        return reply.getResult();
    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 1664);

        System.out.println(client.calc(1, 2, Service.Operation.MUL));
        System.out.println(client.calc(1000, 5, Service.Operation.DIV));

        System.out.println("Fin");
    }

}
