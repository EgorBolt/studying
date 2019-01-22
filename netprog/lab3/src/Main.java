public class Main {

    /*Порядок получения агрументов:
    * название узла
    * процент потерь
    * собственный порт узла
    * (opt)IP родителя узла
    * (opt)порт родителя узла*/
    public static void main(String args[]) {
        Node node = new Node();
        node.start(args);
    }
}
