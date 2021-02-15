package iti.jets.gfive.ui.helpers.serialization;

import iti.jets.gfive.ui.models.serialization.ChatModel;
import iti.jets.gfive.ui.models.serialization.MessageModel;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

// cuz why not
public class Marshaltor {
    public static void main(String[] args) throws JAXBException, IOException {
        var m = new Marshaltor();
//        m.marshalChat();
        m.GenerateHTMLFromXML();
    }

    void marshalChat() throws JAXBException, IOException {
        ChatModel chatModel = new ChatModel();
        chatModel.setChatOwner("ali");
        chatModel.setChatName("SpongeChat");
        chatModel.getMessages().add(new MessageModel("chat", "ali", "spongebob", "sent", "Hello spongebob", new Date()));
        chatModel.getMessages().add(new MessageModel("chat", "spongebob", "ali", "sent", "Hello ali", new Date()));
        JAXBContext jabeontext = JAXBContext.newInstance(ChatModel.class);
        Marshaller marshaller = jabeontext.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        marshaller.marshal(chatModel, new FileWriter("msgs.xml"));


    }

    public void GenerateHTMLFromXML() {
        File xmlFile = new File(getClass().getResource("/msgs.xml").getPath());
        File htmlFile = new File("chatname.html");
        File xsltFile = new File(getClass().getResource("/chat.xslt").getPath());
        try {
            GenerateHTMLFromXMLBasedOnXslt(htmlFile, xmlFile, xsltFile);
        } catch (IOException | ParserConfigurationException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

    private void GenerateHTMLFromXMLBasedOnXslt(File htmlFile, File xmlFile, File xsltFile) throws IOException, SAXException, ParserConfigurationException, TransformerException {
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

    public static Document createDocumentFromFile(File xmlFile) throws ParserConfigurationException, IOException, SAXException {
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
