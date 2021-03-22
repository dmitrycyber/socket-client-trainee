package by.epamtc.client.main;

import by.epamtc.client.service.CloseSocketService;
import by.epamtc.client.service.GetTextService;
import by.epamtc.client.service.RemoveSentencesService;
import by.epamtc.client.service.ServiceFactory;
import by.epamtc.informationHandle.entity.impl.Text;
import by.epamtc.informationHandle.models.MessageType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.stream.LongStream;

public class Client {
    private static Socket clientSocket;
    private static BufferedReader reader;
    private static InputStream inputStream;
    private static OutputStream outputStream;
    private static BufferedReader in;
    private static BufferedWriter out;
    private static ObjectInputStream objIn;
    private static ObjectOutputStream objOut;
    private static final int port = 1234;
    private static String filename = "text.txt";
    private static CloseSocketService closeSocketService;
    private static GetTextService getTextService;
    private static RemoveSentencesService removeSentencesService;
    private static Text text;


    public static void main(String[] args) {
        LongStream.range(1, 3).forEach(index -> {
            try {
                clientSocket = new Socket("localhost", port);
                reader = new BufferedReader(new InputStreamReader(System.in));
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                inputStream = clientSocket.getInputStream();
                outputStream = clientSocket.getOutputStream();

                ObjectMapper objectMapper = new ObjectMapper();

                Wrapper<UserDto> wrapper1 = Wrapper.<UserDto>builder()
                        .messageType("qwe")
                        .message(UserDto.builder()
                                .userId(index)
                                .build())
                        .build();

                Wrapper<UserDto> wrapper2 = Wrapper.<UserDto>builder()
                        .messageType(SocketMessageType.REGISTER)
                        .message(UserDto.builder()
                                .userId(index)
                                .build())
                        .build();

                String message1 = objectMapper.writeValueAsString(wrapper1);
                String message2 = objectMapper.writeValueAsString(wrapper2);

                outputStream.write(message1.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();

                Thread.sleep(5000);

                outputStream.write(message2.getBytes(StandardCharsets.UTF_8));
                outputStream.flush();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                System.out.println("Client is close!");
                try {
                    clientSocket.close();
                    inputStream.close();
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        });

    }

}
