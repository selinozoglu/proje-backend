import com.vdurmont.emoji.EmojiParser;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;
import java.util.Calendar;
import java.util.List;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;





public class Main {
            public static void main(String[] args) throws Exception {
                Connection conn = null;
                String dbServer = "mssql-33403-0.cloudclusters.net"; // change it to your database server name
                int dbPort = 33408; // change it to your database server port
                String dbName = "projectDB";
                String userName = "Selin";
                String password = "Selin2021";
                String url = String.format("jdbc:sqlserver://%s:%d;databaseName=%s;user=%s;password=%s",
                        dbServer, dbPort, dbName, userName, password);
                conn = DriverManager.getConnection(url);
                if (conn != null) {
                    System.out.println("Connected");
                }
                Statement statement = conn.createStatement();
                /*String trainFile = "C:\\Users\\selin\\OneDrive\\Masaüstü\\TemizTweet\\TekKatmanTemizTweet.arff";
                Instances train = getInstance(trainFile);
                int lastIndex = train.numAttributes() - 1;
                train.setClassIndex(lastIndex);

                StringToWordVector stwv = new StringToWordVector();
                stwv.setInputFormat(train);
                train = weka.filters.Filter.useFilter(train, stwv);

                IBk knn = new IBk();
                knn.buildClassifier(train);
                String testFile = "C:\\Users\\selin\\OneDrive\\Masaüstü\\TemizTweet\\test.arff";*/

                ConfigurationBuilder cb = new ConfigurationBuilder();
                cb.setDebugEnabled(true)
                        .setOAuthConsumerKey("zyFnEOgEg4pQHYrcUnLmhyvNu")
                        .setOAuthConsumerSecret("uV7MfOUEze5sQKNzn18f0d0NcbyarwX6khDyVQPWiFmVo2kFCh")
                        .setOAuthAccessToken("1029826700060844032-8BXiLO1UzCR0E3R3O0oFK98xaMVjnD")
                        .setOAuthAccessTokenSecret("tUN8wu1cjlF5eCH2e8ug4H2S5WDxL71i4o4dVicnJO6yu")
                        .setTweetModeExtended(true);
                TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
                //Instances finalTrain = train;
                Connection finalConn = conn;
                //Instances finalTrain = train;
                StatusListener listener = new StatusListener(){
                    public void onStatus(Status status) {

                        String mainTweet = status.getText();
                        mainTweet = mainTweet.replaceAll("(?:#[A-Za-z0-9-_]+)*","");
                        mainTweet = mainTweet.replaceAll("\\n","");
                        mainTweet = mainTweet.replaceAll("[0-9]", "");

                        Pattern p = Pattern.compile("@\\w+ *");
                        Matcher m = p.matcher(mainTweet);
                        mainTweet = m.replaceAll("");

                        Pattern pa = Pattern.compile("http[^ ]*");
                        Matcher ma = pa.matcher(mainTweet);
                        mainTweet= ma.replaceAll("");

                        mainTweet = mainTweet.replaceAll("\\p{Punct}","");
                        mainTweet = mainTweet.toLowerCase();
                        mainTweet = mainTweet.replaceAll("’","");
                        mainTweet = mainTweet.replaceAll("‘","");
                        mainTweet = mainTweet.replaceAll("“","");
                        mainTweet = mainTweet.replaceAll("”","");
                        mainTweet = EmojiParser.removeAllEmojis(mainTweet);

                        List<String> stopwords = null;
                        try {
                            stopwords = Files.readAllLines(Paths.get("C:\\Users\\selin\\IdeaProjects\\BackendServices\\stop-words.txt"));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        String[] cumle = mainTweet.split(" ");

                        StringBuilder builder = new StringBuilder();

                        for(String kelime:cumle){
                            if(!(stopwords.contains(kelime))){
                                builder.append(kelime);
                                builder.append(' ');
                            }
                        }

                        mainTweet = builder.toString().trim();
                        System.out.println(mainTweet);
                        System.out.println("---------------------------------");

                        Calendar cal = Calendar.getInstance();
                        //format date c  dd/MM/yyyyy
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                        //subtract 1 from calendar current date
                        String todaysDate = dateFormat.format(cal.getTime());

                        cal.add(Calendar.DATE, -1);

                        //get formatted date
                        String yesterdayDate=dateFormat.format(cal.getTime());




                        //Date date = status.getCreatedAt();
                        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy MM dd");
                        //String text = date.format(formatter);
                        //LocalDate parsedDate = LocalDate.parse(text, formatter);
                        PreparedStatement prep = null;
                        try {
                            prep = finalConn.prepareStatement("INSERT INTO TweetTablosu( TweetTextHam, TweetAtanKullanici, TweetTarih,TweetAlanSinifID1,TweetAlanSinifID2,SinifID,TweetTextTemiz, TweetAtilanil_ID) VALUES(?, ?, ?, ?, ?,?,?,?)");
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            prep.setString(1, status.getText());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            prep.setString(2, status.getUser().getScreenName());
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            prep.setString(3, todaysDate);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            prep.setString(4, String.valueOf(1));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            prep.setString(5, String.valueOf(2));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            prep.setString(6, String.valueOf(3));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            prep.setString(7, mainTweet);
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            prep.setString(8, String.valueOf(22));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }
                        try {
                            prep.executeUpdate();
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                        }


                        FileWriter arff = null;
                        try {
                            arff = new FileWriter("C:\\Users\\selin\\OneDrive\\Masaüstü\\TemizTweet\\test.arff");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {
                            arff.append("@relation ilkkatman\n\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            arff.append("@attribute text string\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            arff.append(
                            "@attribute class{Ekonomi_Olumsuz,Ekonomi_Tarafsiz,Doviz_Diger_Tarafsiz,Borsa_Tarafsiz,Enflasyon_Tarafsiz,Ekonomi_Olumlu,Ekonomi_Alakasiz,Doviz_Dolar_Olumsuz,Doviz_Dolar_Olumlu,Borsa_Olumsuz,Doviz_Diger_Olumsuz,Borsa_Olumlu,Altin_Tarafsiz,Enflasyon_Olumsuz,Enflasyon_Olumlu,Alakasiz,Doviz_Euro_Alakasiz,Doviz_Euro_Tarafsiz,Altin_Olumlu,Doviz_Dolar_Tarafsiz,Doviz_Diger_Olumlu,Altin_Olumsuz,Doviz_Euro_Olumsuz,Borsa_Alakasiz,Altin_Alakasiz,Doviz_Diger_Alakasiz,Doviz_Dolar_Alakasiz,Enflasyon_Alakasiz,Doviz_Euro_Olumlu}\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            arff.append("@data\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        try {

                                arff.append("'" + mainTweet + "'" + ",?\n");


                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            arff.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            arff.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                       /* while(true)	{
                            if(new File(testFile).exists())	{
                                List<String> allLines = null;
                                try {
                                    allLines = Files.readAllLines(Paths.get(testFile));
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Test verisi okuması yapılıyor..");

                                Instances test = null;
                                try {
                                    test = getInstance(testFile);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                test.setClassIndex(lastIndex);
                                try {
                                    test = weka.filters.Filter.useFilter(test, stwv);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                System.out.println("Test verisi üzerinde String to word Vektor işlemi yapıldı...");

                                System.out.println("Sınıflandırma işlemine başlanıyor");
                                for (int i = 0; i < test.numAttributes(); i++) {
                                    double index0 = 0;
                                    try {
                                        index0 = knn.classifyInstance(test.instance(i));
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    String className = finalTrain.attribute(0).value((int) index0);
                                    System.out.println(mainTweet + "Sınıflandırma Sonucu: " + className + " - ");
                                    System.out.println("---------------------------------");


                                }

                            }
                        }*/

                       /* System.out.println(status.getUser().getLocation());//tr
                        System.out.println("-------------------------------");
                        System.out.println(status.getLang());//tr
                        System.out.println("-------------------------------");
                        System.out.println(status.getCreatedAt());//Mon May 10 19:05:52 TRT 2021
                        System.out.println("-------------------------------");
                        System.out.println(status.getUser().getScreenName());//eroldogan29
                        System.out.println("-------------------------------");
                        System.out.println(status.getFavoriteCount());//0
                        System.out.println("-------------------------------");
                        System.out.println(status.getText());//RT @penceretv2: BIST100 Yüzde 1.36 Yükseldi, Dolar 8.27 Lirada https://t.co/cjbG8eoW5e
                        System.out.println("-------------------------------");
                        System.out.println(status.getUser().getName());//Can Güven
                        System.out.println("-------------------------------");*/

                    }
                    public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {
                        System.out.println("Got a status deletion notice id:" + statusDeletionNotice.getStatusId());
                    }
                    public void onTrackLimitationNotice(int numberOfLimitedStatuses) {
                        System.out.println("Got track limitation notice:" + numberOfLimitedStatuses);
                    }

                    @Override
                    public void onScrubGeo(long l, long l1) {
                        System.out.println("Got scrub_geo event userId:" + l + " upToStatusId:" + l1);

                    }

                    @Override
                    public void onStallWarning(StallWarning stallWarning) {
                        System.out.println("Got stall warning:" + stallWarning);
                    }

                    public void onException(Exception ex) {
                        ex.printStackTrace();
                    }
                };
                //Query query = new Query("ekonomi" + " -filter:retweets");
                FilterQuery fq = new FilterQuery();
                String keywords[] = {"altın","borsa","ekonomi","doviz","enflasyon"};
                String[] lang = {"tr"};
                fq.language(lang);
                fq.track(keywords);

                twitterStream.addListener(listener);
                twitterStream.filter(fq);

            }
            /*private static Instances getInstance(String fileName) throws IOException {
                BufferedReader reader = new BufferedReader(new FileReader(fileName));
                Instances instance = new Instances(reader);
                reader.close();

                return instance;
            }*/
        }


  /*  String mainTweet = tweet.getText();


                    mainTweet = mainTweet.replaceAll("(?:#[A-Za-z0-9-_]+)*","");
                            mainTweet = mainTweet.replaceAll("\\n","");
                            mainTweet = mainTweet.replaceAll("[0-9]", "");

                            Pattern p = Pattern.compile("@\\w+ *");
                            Matcher m = p.matcher(mainTweet);
                            mainTweet = m.replaceAll("");

                            Pattern pa = Pattern.compile("http[^ ]*");
                            Matcher ma = pa.matcher(mainTweet);
                            mainTweet= ma.replaceAll("");

                            mainTweet = mainTweet.replaceAll("\\p{Punct}","");
                            mainTweet = mainTweet.toLowerCase();
                            mainTweet = mainTweet.replaceAll("’","");
                            mainTweet = mainTweet.replaceAll("‘","");
                            mainTweet = mainTweet.replaceAll("“","");
                            mainTweet = mainTweet.replaceAll("”","");
                            mainTweet = EmojiParser.removeAllEmojis(mainTweet);

                            var stopwords = Files.readAllLines(Paths.get("C:\\Users\\selin\\IdeaProjects\\BackendServices\\stop-words.txt"));
                            String[] cumle = mainTweet.split(" ");

                            StringBuilder builder = new StringBuilder();

                            for(String kelime:cumle){
                            if(!(stopwords.contains(kelime))){
                            builder.append(kelime);
                            builder.append(' ');
                            }
                            }

                            mainTweet = builder.toString().trim();

                            String arffyeKoyulacakTweet = "'" + mainTweet +"'"+ ",'Ekonomi'";

                            FileWriter arff = new FileWriter("C:\\Users\\selin\\OneDrive\\Masaüstü\\TemizTweet\\test.arff");


                            arff.append("@relation ilkkatman\n");
                            arff.append("@attribute text string\n");
                            arff.append("@attribute class{Altin,Alakasiz,Borsa,Ekonomi,Enflasyon,Doviz}\n");
                            arff.append("@data\n");
                            arff.append("'"+mainTweet+"'"+",");
                            arff.append("?");
                            arff.flush();
                            arff.close();




                            System.out.println("'" + mainTweet +"'"+ ",'Ekonomi'");
//System.out.println(" ");
//System.out.println(tweeetler);
//"@" + tweet.getUser().getScreenName()  */