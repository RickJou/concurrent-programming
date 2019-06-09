package concurrent.countdownlatch.http;

public class PostHttpThread extends Thread{

    private final CloseableHttpClient httpClient;
    private final HttpContext context;
    private final HttpGet httpget;
    private final int id;

    public PostHttpThread(CloseableHttpClient httpClient, HttpGet httpget, int id) {
        this.httpClient = httpClient;
        this.context = new BasicHttpContext();
        this.httpget = httpget;
        this.id = id;
    }

    @Override
    public void run() {
        super.run();
    }
}
