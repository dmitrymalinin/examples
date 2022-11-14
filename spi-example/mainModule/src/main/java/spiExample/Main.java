package spiExample;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.logging.Logger;

public class Main {
	private static final Logger logger = Logger.getLogger(Main.class.getName());
	
	public static void main(String[] args) throws Exception {
		final Properties config = new Properties();
		config.load(Main.class.getResourceAsStream("/app.properties"));
		String providersDir = config.getProperty("ProvidersDir");
		logger.info("providersDir="+providersDir);
		String helloServiceProviderName = config.getProperty("HelloServiceProvider.name");
		logger.info("helloServiceProviderName="+helloServiceProviderName);
				
		Path providersDirPath = Paths.get(providersDir);
		if (!providersDirPath.isAbsolute())
			providersDirPath = Paths.get("").resolve(providersDirPath);
		logger.info("providersDirPath="+providersDirPath.toAbsolutePath());
		
		final URLClassLoader classLoader;
		if (Files.exists(providersDirPath))
		{
			classLoader = createClassLoader(providersDirPath.toFile());
		} else
		{
			logger.warning("Каталог для провайдеров не существует: "+providersDir+". Будет использован системный загрузчик классов.");
			classLoader = null;
		}
		
		final ServiceLoader<HelloServiceProviderFactory> helloServiceProviderFactoryLoader = ServiceLoader.load(HelloServiceProviderFactory.class, classLoader);
		
		HelloServiceProviderFactory helloServiceProviderFactory = null;
		
		if (helloServiceProviderName != null)
		{
			for (HelloServiceProviderFactory f: helloServiceProviderFactoryLoader)
			{
				if (helloServiceProviderName.equals(f.getName()))
				{
					helloServiceProviderFactory = f;
					logger.info("Найден HelloServiceProviderFactory: "+helloServiceProviderFactory.getClass().getName());
					break;
				}
			}
			
			if (helloServiceProviderFactory == null)
				logger.warning(helloServiceProviderName+" не найден. Будет использован DefaultHelloServiceProvider по умолчанию.");
		}
		
		if (helloServiceProviderFactory == null)
			helloServiceProviderFactory = new DefaultHelloServiceProviderFactory();
		
		helloServiceProviderFactory.init(config);
		
		HelloServiceProvider sp = helloServiceProviderFactory.create();
		
		Thread.sleep(1000);
		System.out.println(sp.getClass().getName()+" say: "+sp.hello());
	}
	
	private static URLClassLoader createClassLoader(File dir) throws MalformedURLException 
	{
		final List<URL> urls = new LinkedList<URL>();
		for (File f: dir.listFiles((d, name) -> name.toLowerCase().endsWith(".jar")))
		{
			if (f.isFile())
			{
				urls.add(f.toURI().toURL());
			}
		}

		logger.info("Загрузка провайдеров из " + urls.toString());

		return new URLClassLoader(urls.toArray(new URL[urls.size()]));
    }
		
}
