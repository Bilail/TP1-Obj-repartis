import fr.polytech.grpc.proto.Service.*;
import fr.polytech.grpc.proto.ProtoGrpc;
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

    /**
     *
     * @param inf les informations de la personne qui envoie les données
     * @param data les données envoyées
     * @return renvoie la réponse
     */
    public Reply sendDatatoServer(Info inf, Map<String, Data> data){
         Request request = Request.newBuilder().setInfo(inf).putAllData(data).build();
         Reply reply = blockingStub.handle(request);
         return reply;

    }

    /**
     *  Fonction Main on test avec 2 cas, un cas avec des datas et un
     *  cas avec data vide qui lève un message d'erreur
     * @param args
     */
    public static void main(String[] args) {
        Client client = new Client("localhost", 1664);

        System.out.println(" ----------- Test 1  ----------- ");
        Info info = Info.newBuilder().setSender("Dupont").setTimestamp(10101010).setId(101).build();

        Data d1 = Data.newBuilder().setData1((float)1.5).setData2(true).addAllData3(new ArrayList<>(Arrays.asList(1, 2, 3))).build();
        Data d2 = Data.newBuilder().setData1((float)1.7).setData2(false).addAllData3(new ArrayList<>(Arrays.asList(4, 5, 6))).build();

        Map<String, Data> dataMap = new HashMap<>();
        dataMap.put("test1", d1);
        dataMap.put("test2", d2);
        System.out.println(client.sendDatatoServer(info, dataMap));

        System.out.println(" \n\n----------- Test 2  ----------- ");

        Info info2 = Info.newBuilder().setSender("Benoit").setTimestamp(1575640).setId(103).build();
        System.out.println(client.sendDatatoServer(info2, new HashMap<>()));


        System.out.println("Fin");
    }

}
