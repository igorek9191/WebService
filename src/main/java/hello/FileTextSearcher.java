package hello;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by IVlasyuk on 20.01.2018.
 */
public class FileTextSearcher {

    protected static List<String> result = new ArrayList<String>();

    private static ExecutorService pool = Executors.newFixedThreadPool(4);

    private static String searchingText;

    public static List<String> mainFunction(String text) throws IOException, ExecutionException, InterruptedException {

        searchingText = text;

        //для каждого рутового диска...
        for(File rootDir :  File.listRoots()){

            //получаем массив названий того, что содержится в корневом диске
            String[] filesAndDirectoriesInRoot = rootDir.list();

            if(filesAndDirectoriesInRoot != null) {

                //запускаем задачу в пул, в которую передаём i-ое название содержимого корневого диска
                for (int i = 0; i < filesAndDirectoriesInRoot.length; i++) {

                    pool.submit(new MyRunnable(rootDir.getAbsolutePath() + filesAndDirectoriesInRoot[i]));
                }

            } else System.err.println("Disc "+rootDir+" has no content");
        }

        pool.shutdown();
        pool.awaitTermination(1, TimeUnit.HOURS);

        return result;
    }

    public static class MyRunnable implements Runnable {

        Path pathToDirectory;
        //При создании экземпляра устанавливаем значение Path целевого каталога
        public MyRunnable(String pathToDirectory){
            Path path = Paths.get(pathToDirectory).toAbsolutePath();
            this.pathToDirectory = path;
        }

        @Override
        public void run() {
            try {
                Files.walkFileTree(pathToDirectory, new MyFileVisitor(searchingText));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

/*    private static List<String> reduceRepeats(List<String> list){
        for(int i = list.size()-1; i>0; i--){
            if(list.get(i).contains(list.get(i-1))) list.remove(i-1);
        }
        return list;
    }*/

}

        /*DirectoryStream<Path> entries = Files.newDirectoryStream(Paths.get("C:\\"));
        for(Path path : entries){
            System.out.println(path.getFileName());
        }*/

