package com.loacg;

import com.loacg.bootstrap.App;
import com.loacg.dao.OwnerDao;
import com.loacg.entity.FileDown;
import com.loacg.entity.Owner;
import com.loacg.github.SyncDaemon;
import com.loacg.github.entity.Asset;
import com.loacg.github.entity.Release;
import com.loacg.utils.Downloads;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.util.List;

@SpringBootApplication
public class SyncGithub {

	@Autowired
	SyncDaemon daemon;

	@Autowired
	OwnerDao ownerDao;

	@Autowired
	Downloads downloads;

	public static void main(String[] args) {
		SpringApplication.run(SyncGithub.class, args);
	}

	@Bean
	public EmbeddedServletContainerCustomizer containerCustomizer() {

		return (container -> {
			ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/401.html");
			ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404.html");
			ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500.html");

			container.addErrorPages(error401Page, error404Page, error500Page);
		});
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@PostConstruct
	public void start() {

		List<Owner> ownerList = ownerDao.getAll();
		for (Owner owner: ownerList) {
			Release[] releases = daemon.getList(owner.getUserName(), owner.getRepoName());
			for (Release release : releases) {
				List<Asset> assets = release.getAssets();
				if (assets == null || assets.size() <= 0) {
					// archive
					String fileName = release.getTagName() + ".zip";
					FileDown fileDown = new FileDown(
							fileName,
							-1L,
							release.getZipballUrl(),
							App.DEFAULT_PATH +
									owner.getUserName() + File.separator +
									owner.getRepoName() + File.separator +
									release.getTagName() + File.separator +
									fileName);
					downloads.add(fileDown);
					continue;
				}

				assets.stream().filter(asset -> !StringUtils.isEmpty(
						asset.getBrowserDownloadUrl())).forEach(asset -> {
					FileDown fileDown = new FileDown(
							asset.getName(),
							asset.getSize(),
							asset.getBrowserDownloadUrl(),
							App.DEFAULT_PATH +
									owner.getUserName() + File.separator +
									owner.getRepoName() + File.separator +
									release.getTagName() + File.separator +
									asset.getName());
					downloads.add(fileDown);
				});
			}
		}

		downloads.start();
	}

	@PreDestroy
	public void destroy() {

	}
}
