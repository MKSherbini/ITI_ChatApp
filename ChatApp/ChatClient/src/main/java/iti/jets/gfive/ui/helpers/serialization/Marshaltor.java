package iti.jets.gfive.ui.helpers.serialization;

import iti.jets.gfive.common.models.UserDto;
import iti.jets.gfive.services.UserDBCrudService;
import iti.jets.gfive.ui.models.chat.ChatModel;
import iti.jets.gfive.ui.models.chat.MessageModel;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.DirectoryChooser;
import org.apache.commons.io.FileUtils;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;

// cuz why not
public class Marshaltor {
    public static Marshaltor instance;
    private final DirectoryChooser directoryChooser;

//    public static void main(String[] args) throws JAXBException, IOException {
//        var m = getInstance();
//        m.marshalChat();
//    }

    private Marshaltor() {
        directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select html export folder");
//        FileChooser.ExtensionFilter htmlFilter = new FileChooser.ExtensionFilter(
//                "Web image", "html");
//        directoryChooser.getExtensionFilters().add(htmlFilter);
//        directoryChooser.setSelectedExtensionFilter(htmlFilter);
    }

    public synchronized static Marshaltor getInstance() {
        if (instance == null) {
            return instance = new Marshaltor();
        }
        return instance;
    }


    public void marshalChat(ChatModel chatModel) {
//        ChatModel chatModel = new ChatModel();
//        chatModel.setChatOwner("ali");
//        chatModel.setChatName("SpongeChat2");
//        chatModel.getMessages().add(new MessageModel("chat", "ali", "spongebob", "sent", "Hello spongebob", new Date()));
//        chatModel.getMessages().add(new MessageModel("chat", "spongebob", "ali", "sent", "Hello ali", new Date()));

        // getScene().getWindow()
        File outputHtml = directoryChooser.showDialog(null);
        if (outputHtml == null) return;

        JAXBContext jabeontext = null;
        ByteArrayOutputStream out = null;
        ByteArrayInputStream pipe = null;
        try {
            prepareFolder(outputHtml, chatModel);
            prepareMessage(outputHtml, chatModel);
            jabeontext = JAXBContext.newInstance(ChatModel.class);
            Marshaller marshaller = jabeontext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            out = new ByteArrayOutputStream();
            marshaller.marshal(chatModel, out);
//            marshaller.marshal(chatModel, new FileWriter("chat.xml"));

            pipe = new ByteArrayInputStream(out.toByteArray());
            GenerateHTMLFromXML(outputHtml, pipe);

        } catch (JAXBException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                out.close();
                pipe.close();
            } catch (Exception e) {
                // idc, do your worse
            }
        }
    }

    private void prepareFolder(File dirTarget, ChatModel chatModel) throws IOException {
        FileUtils.copyDirectory(new File(this.getClass().getResource("/chatExportFiles").getPath()), dirTarget);
    }

    private void prepareMessage(File dirTarget, ChatModel chatModel) {
        Map<String, UserDto> userMap = new HashMap<>();
        chatModel.getMessages().forEach(messageModel -> {
            var receiver = setupUser(userMap, dirTarget, messageModel.getReceiverPhone());
            var sender = setupUser(userMap, dirTarget, messageModel.getSenderPhone());
            messageModel.setImage(sender.getUsername() + ".png");
            messageModel.setReceiverName(receiver.getUsername());
            messageModel.setSenderName(sender.getUsername());
        });
    }


    void saveImage(UserDto user, File dirTarget) {
        try {
            System.out.println("user==null = " + (user == null));
            System.out.println("user.getImage()==null = " + (user.getImage() == null));

            var file = new File(String.format("%s/assets/%s.png", dirTarget.getAbsolutePath(), user.getUsername()));
            file.createNewFile();
            ImageIO.write(SwingFXUtils.fromFXImage(user.getImage(), null), "png", file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    UserDto setupUser(Map<String, UserDto> userMap, File dirTarget, String user) {
        if (!userMap.containsKey(user)) {
            try {
                System.out.println("user = " + user);
                var userDto = UserDBCrudService.getUserService().selectFromDB(user); // todo check nulls
                userMap.put(user, userDto);
                saveImage(userDto, dirTarget);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        return userMap.get(user);
    }

    private void GenerateHTMLFromXML(File htmlFile, ByteArrayInputStream xmlFile) {
        File xsltFile = new File(getClass().getResource("/chat.xslt").getPath());
        try {
//            if (htmlFile.getName().toLowerCase(Locale.ROOT).endsWith(".html"))
//                htmlFile.createNewFile();
//            else {
//                htmlFile = new File(htmlFile.getAbsolutePath() + ".html");
//                htmlFile.createNewFile();
//            }
//            System.out.println(this.getClass().getResource("/chatExportFiles").getPath());
//            System.out.println(htmlFile.getAbsolutePath());
            htmlFile = new File(htmlFile.getAbsolutePath() + "/chat.html");
            htmlFile.createNewFile();
            GenerateHTMLFromXMLBasedOnXslt(htmlFile, xmlFile, xsltFile);
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private void GenerateHTMLFromXMLBasedOnXslt(File htmlFile, ByteArrayInputStream xmlFile, File xsltFile) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        Document xmlDocument = createDocumentFromFile(xmlFile);
        DOMSource xmlDomSource = new DOMSource(xmlDocument);

        StreamResult streamResult = new StreamResult(htmlFile);

        Document xsltDocument = createNSAwareDocumentFromFile(xsltFile);
        DOMSource xsltDomSource = new DOMSource(xsltDocument);

        transform(xsltDomSource, xmlDomSource, streamResult);
    }

    private void transform(DOMSource xsltDomSource, DOMSource xmlDomSource, StreamResult streamResult) throws TransformerException {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer(xsltDomSource);
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(xmlDomSource, streamResult);
    }

    public static Document createDocumentFromFile(ByteArrayInputStream xmlFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        return documentBuilder.parse(xmlFile);
    }

    private Document createNSAwareDocumentFromFile(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder documentBuilder = factory.newDocumentBuilder();
        return documentBuilder.parse(xmlFile);
    }
}
