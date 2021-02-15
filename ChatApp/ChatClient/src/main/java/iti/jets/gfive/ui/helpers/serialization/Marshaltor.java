package iti.jets.gfive.ui.helpers.serialization;

import iti.jets.gfive.ui.models.chat.ChatModel;
import iti.jets.gfive.ui.models.chat.MessageModel;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.commons.io.FileUtils;
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
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

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


    public void marshalChat() {
        ChatModel chatModel = new ChatModel();
        chatModel.setChatOwner("ali");
        chatModel.setChatName("SpongeChat2");
        chatModel.getMessages().add(new MessageModel("chat", "ali", "spongebob", "sent", "Hello spongebob", new Date()));
        chatModel.getMessages().add(new MessageModel("chat", "spongebob", "ali", "sent", "Hello ali", new Date()));

        // getScene().getWindow()
        File outputHtml = directoryChooser.showDialog(null);
        if (outputHtml == null) return;

        JAXBContext jabeontext = null;
        ByteArrayOutputStream out = null;
        ByteArrayInputStream pipe = null;
        try {
            jabeontext = JAXBContext.newInstance(ChatModel.class);
            Marshaller marshaller = jabeontext.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            out = new ByteArrayOutputStream();
            marshaller.marshal(chatModel, out);
            pipe = new ByteArrayInputStream(out.toByteArray());
            GenerateHTMLFromXML(outputHtml, pipe);
        } catch (JAXBException e) {
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

    public void GenerateHTMLFromXML(File htmlFile, ByteArrayInputStream xmlFile) {
        File xsltFile = new File(getClass().getResource("/chat.xslt").getPath());
        try {
//            if (htmlFile.getName().toLowerCase(Locale.ROOT).endsWith(".html"))
//                htmlFile.createNewFile();
//            else {
//                htmlFile = new File(htmlFile.getAbsolutePath() + ".html");
//                htmlFile.createNewFile();
//            }
            System.out.println(this.getClass().getResource("/chatExportFiles").getPath());
            System.out.println(htmlFile.getAbsolutePath());
            FileUtils.copyDirectory(new File(this.getClass().getResource("/chatExportFiles").getPath()), htmlFile);
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
