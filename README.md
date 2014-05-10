What is it?
===========

A good-looking frontend for Google's Picasaweb photo galleries, with nice transitions,
mobile device support, and full-screen photo browsing with quick loading (prefetching).

For all those who wish their Picasaweb gallery had black background!

Example gallery: [Anton Keks Photos](http://photos.azib.net/) - use the link at the bottom to try it with your own gallery!

How does it work?
=================

It's a small Java application made to be hosted for free on Google App Engine.
It uses Google Data API to fetch and display your albums and photos, so whenever you change anything
on Picasaweb, it will become visible in this gallery.

Features
========

- Dark theme that emphasizes photos
- Shows your exising Picasaweb albums, no additional storage needed
- Search within the gallery (by tags, keywords, descriptions, etc)
- Nice bookmarkable URLs for albums, individual photos, search results
- Gallery map if albums are geotagged, album map coming soon
- Full-screen photo viewer
- Fast: minimum number of requests, optimized caching, preloading of next photos
- No pagination (but only visible thumbnails are loaded)
- Keyboard navigation
- Mobile device support, eg iPhone, iPad, Android (including touch events)
- Slideshow with adjustable delay
- Facebook like buttons for albums and individual photos
- Opengraph metadata (for FB sharing, etc)
- Google analytics support
- Viewing of other people's albums, just add "?by=username" parameter
- Showing of a single (weighted) random photo from all albums, just add "?random" parameter

How to use it for your own photos
=================================

- Clone this repository as described on Github
- Specify your Picasaweb username in src/config.properties
- Specify your AppEngine application ID in web/WEB-INF/appengine-web.xml
- Download AppEngine SDK from Google
- If you don't use any IDE, use ant to compile by this command:
  
  	    $ ant -Dsdk.dir=path/to/appengine-java-sdk compile

- Use AppEngine SDK / Eclipse / IntelliJ IDEA to deploy your application.
  Details here: <http://code.google.com/appengine/docs/java/gettingstarted/uploading.html>
P.S. the app is a standard Java Servlet-based application, so it will work without AppEngine as well.


Without Google AppEngine (by footstone)
=================================
- 考虑到不需要GAE环境的情况，可以通过以下代码打包：

```
	ant -f simple-build.xml
```
- 考虑到picasa图库可能被墙的风险，增加墙内镜像的配置：

```
	image.url: 镜像图库域名地址(如http://x.com/photos),多个地址之间用";"间隔（不需要镜像的忽略）
	local.path: 镜像服务器上相册地址(如/home/app/photos),在同步比较相册时使用（不需要镜像的忽略）
```
- 墙内镜像图库同步方式，在镜像服务器上建立定时任务，执行以下脚本：

```
	java -classpath ${CLASSPATH} com.footstone.photos.proc.Synchronize sync
```
- 在heroku上部署该应用，参考[这里](http://footstone.github.io/2014/05/08/deploy-gallery-on-heroku/) 




