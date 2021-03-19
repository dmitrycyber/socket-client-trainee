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


    public static void main(String[] args) throws InterruptedException {
        try {
            clientSocket = new Socket("localhost", port);
            reader = new BufferedReader(new InputStreamReader(System.in));
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            inputStream = clientSocket.getInputStream();
            outputStream = clientSocket.getOutputStream();
//            objIn = new ObjectInputStream(inputStream);
//            objOut = new ObjectOutputStream(outputStream);
//            closeSocketService = ServiceFactory.closeSocketService(objOut);
//            getTextService = ServiceFactory.getTextService(objIn, objOut);
//            removeSentencesService = ServiceFactory.removeSentencesService(objIn, objOut);

            ObjectMapper objectMapper = new ObjectMapper();

            Wrapper<UserDto> wrapper = Wrapper.<UserDto>builder()
                    .messageType(SocketMessageType.REGISTER)
                    .message(UserDto.builder()
                            .userId(1L)
                            .build())
                    .build();

            String message = objectMapper.writeValueAsString(wrapper);

//            String message = "hello";


            outputStream.write(message.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();

//            while (true) {
//                Thread.sleep(1000);
//                System.out.println("Write your mess to server:");


//                String messageType = reader.readLine();
//                if (messageType.equals(MessageType.STOP_MESSAGING)) {
//                    closeSocketService.closeSocket();
//                    break;
//                }
//                if (messageType.equals(MessageType.FILE_OBJECT)){
//                    String file = Client.class.getClassLoader().getResource(filename).getPath();
//                    text = getTextService.getText(file);
//                }
//                if (messageType.equals(MessageType.REMOVE_WORDS_FIXED_LENGTH)){
//                    System.out.println("Input word length: ");
//                    int wordLength = Integer.parseInt(reader.readLine());
//                    if (text != null){
//                        Text text = removeSentencesService.removeFixedLengthWordText(Client.text, wordLength);
//                        System.out.println(text);
//                    }
//                    else {
//                        System.out.println("Text is null!!");
//                    }
//                }
//            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
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
    }

}
