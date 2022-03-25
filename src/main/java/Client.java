import fr.polytech.grpc.proto.Service.*;
import fr.polytech.grpc.proto.ProtoGrpc;
import fr.polytech.grpc.proto.Service.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Client {

    private ManagedChannel channel;
    protected ProtoGrpc.ProtoBlockingStub blockingStub;

    public Client(String host, int port) {
        channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        initStubs(channel);
    }

    protected void initStubs(ManagedChannel channel) {

        blockingStub = ProtoGrpc.newBlockingStub(channel);
    }

    public Reply f(Info inf, Map<String, Data> data){
         Request request = Request.newBuilder().setInfo(inf).putAllData(data).build();
         Reply reply = blockingStub.handle(request);
         return reply;

    }

    public static void main(String[] args) {
        Client client = new Client("localhost", 1664);

        Info i = Info.newBuilder().setSender("Dupont").setTimestamp(0101010).setId(01).build();

        //Data d1 = Data.newBuilder().setData1((float)1.5).setData2(true).setData3(new ArrayList<int>(Arrays.asList(1,2,3)));
        Data.Builder d1 = Data.newBuilder().setData1((float)1.5).setData2(true).addAllData3(new ArrayList<int>(Arrays.asList(1,2,3)));


        Map<String, Data> d = (Map<String, Data>) new HashMap<>().put("test",d1);
        System.out.println(client.f(i,d));


        System.out.println("Fin");
    }

}
