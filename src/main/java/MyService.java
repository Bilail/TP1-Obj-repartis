import fr.polytech.grpc.proto.ProtoGrpc;
import fr.polytech.grpc.proto.Service.*;

import io.grpc.stub.StreamObserver;

import java.util.Map;


public class MyService extends ProtoGrpc.ProtoImplBase {

    public MyService() {
    }

    @Override
    public void handle(Request input, StreamObserver<Reply> responseObserver) {
        Info info = input.getInfo();
        Map<String, Data> dataMap = input.getDataMap();

        StringBuilder stb = new StringBuilder("=====RECEIVED MESSAGE FROM CLIENT=====");
        stb.append("Sender ").append(info.getSender()).append("\n");
        stb.append("Timestamp: ").append(info.getTimestamp()).append("\n");
        stb.append("ID: ").append(info.getId()).append("\n");
        stb.append("\n");
        stb.append("---DECODED DATA MAP---").append("\n");
        stb.append(handleDataMap(dataMap)).append("\n");

        System.out.println("MyService " + this + "  " + stb);

        long now = System.currentTimeMillis()/1000L;

        Reply.Builder replyBuilder = Reply.newBuilder()
                .setInfo(Info.newBuilder().setSender("Server").setTimestamp(now).setId(1))
                .setInfo(info)
                .setHandled(dataMap.size());

        if (dataMap.isEmpty()){
            replyBuilder.setMessage("ERROR: NO DATA GIVEN");
            replyBuilder.setStatus(0);
        } else{
            replyBuilder.setMessage("HANDELED " + dataMap.size());
            replyBuilder.setStatus(-1);
        }

        responseObserver.onNext(replyBuilder.build());
        responseObserver.onCompleted();
    }

    private String handleDataMap(Map<String, Data> dataMap) {
        StringBuilder stb = new StringBuilder();
        for (Map.Entry<String, Data> entry : dataMap.entrySet()) {
            Data data = entry.getValue();
            stb.append(entry.getKey()).append(":").append(data.getData1())
                    .append(", ").append(data.getData2())
                    .append(", ").append(data.getData3List()).append("\n");
        }

        return stb.toString();
    }

}
