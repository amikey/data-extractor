
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Scanner;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class DataExtractor {
    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public int saveAsCSV(File output) throws IOException{
        LinkedHashSet<String> words = new LinkedHashSet<String>();
        Scanner reader = new Scanner(content);
        while(reader.hasNext()){
            String next = reader.next();
            char lastChar = next.charAt(next.length()-1);
            next = next.replaceAll("[.,()\"]","");
            if(next.replaceAll("\\W","").length() > 0) words.add(next);
        }
            if (!output.exists()) {
                output.createNewFile();
            }

            FileWriter fw = new FileWriter(output.getAbsoluteFile().toString());
            BufferedWriter bw = new BufferedWriter(fw);
            Iterator<String> itr = words.iterator();
            while (itr.hasNext()) {
                String word = (String) itr.next();
                bw.write(word);
                if (itr.hasNext()) bw.write(",");
            }
            bw.close();
            reader.close();
        return words.size();
    }

    private String title, author, date, content;

    public DataExtractor(File fInput) {
        WebClient client = new WebClient();
        client.setJavaScriptEnabled(false);
        client.setCssEnabled(false);
        client.setThrowExceptionOnFailingStatusCode(false);
        client.setThrowExceptionOnScriptError(false);
        client.setPrintContentOnFailingStatusCode(false);

        HtmlPage currentPage = null;
        try {
            currentPage = client.getPage(fInput.toURL());

            HtmlElement hTitle = (HtmlElement) currentPage.getFirstByXPath("//div[@id=\"content\"]/h1");
            title = hTitle.asText();

            HtmlElement hDate = (HtmlElement) currentPage.getFirstByXPath("//div[@id=\"content\"]/p");
            date = hDate.asText();

            HtmlElement hContent = (HtmlElement) currentPage.getFirstByXPath("//div[@id=\"content\"]/div[@class=\"base\"]");
            content = hContent.asText();

            List<HtmlElement> hAuthors = (List<HtmlElement>) currentPage.getByXPath("//div[@id=\"content\"]/div/p/a");
            if (hAuthors.size() == 0) {
                HtmlElement hAuthor = (HtmlElement) currentPage.getFirstByXPath("//div[@id=\"content\"]/div[@class=\"base\"]/p/b");
                author = hAuthor.asText();
                content = content.replace(author,"");
                author = author.substring(1, author.length() - 1);
            } else {
                author = new String();
                Iterator<HtmlElement> itr = hAuthors.iterator();
                while (itr.hasNext()) {
                    author += itr.next().asText();
                    if (itr.hasNext()) author += "; ";
                }
            }
            content = content.substring(0,content.lastIndexOf(".")+1);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
