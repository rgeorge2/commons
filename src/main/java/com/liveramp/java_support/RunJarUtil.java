package com.liveramp.java_support;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.regex.Pattern;

import com.google.common.base.Optional;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.liveramp.java_support.util.JarUtils;

public class RunJarUtil {
  private static final Logger LOG = LoggerFactory.getLogger(RunJarUtil.class);

  private static final Pattern JOB_JAR_MATCHER = Pattern.compile(".*\\.job.jar");
  public static final String SCM_REMOTE_ENV_VAR = "SCM_REMOTE";
  public static final String SCM_REVISION_ENV_VAR = "SCM_REVISION";

  public static class ScmInfo {
    private final String gitRemote;
    private final String revision;

    public ScmInfo(String gitRemote, String revision) {
      this.gitRemote = gitRemote;
      this.revision = revision;
    }

    public String getGitRemote() {
      return gitRemote;
    }

    public String getRevision() {
      return revision;
    }

    @Override
    public String toString() {
      return "ScmInfo{" +
          "gitRemote='" + gitRemote + '\'' +
          ", revision='" + revision + '\'' +
          '}';
    }
  }

  private static URL[] getClasspathJars() {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    URLClassLoader loader1 = (URLClassLoader)loader;
    return loader1.getURLs();
  }

  public static ScmInfo getRemoteAndCommit() {
    Optional<ScmInfo> scmInfo = getScmInfoFromLaunchJar();
    if (scmInfo.isPresent()) {
      LOG.error("Found SCM info: " + scmInfo.get());
      return scmInfo.get();
    } else {
      scmInfo = getScmInfoFromEnv();
      if (scmInfo.isPresent()) {
        LOG.error("Found SCM info: " + scmInfo.get());
        return scmInfo.get();
      }
    }
    LOG.error("Failed to find MANIFEST containing SCM info. Please ensure that the classpath contains a MANIFEST.MF file with scm information, or that the environment has been set");
    return new ScmInfo(null, null);
  }

  private static Optional<ScmInfo> getScmInfoFromEnv() {
    String remote = System.getenv(SCM_REMOTE_ENV_VAR);
    String revision = System.getenv(SCM_REVISION_ENV_VAR);
    if (remote != null && revision != null) {
      return Optional.of(new ScmInfo(remote, revision));
    } else {
      return Optional.absent();
    }
  }

  public static Optional<ScmInfo> getScmInfoFromLaunchJar() {
    try {
      URL url = RunJarUtil.getLaunchJarURL();
      if (url != null) {
        JarFile jar = new JarFile(new File(url.toURI()));
        Attributes jarAttributes = new Manifest(
            jar.getInputStream(jar.getEntry("META-INF/MANIFEST.MF"))
        ).getMainAttributes();
        ScmInfo scmInfo = new ScmInfo(jarAttributes.getValue("Remote"), jarAttributes.getValue("Implementation-Build"));

        LOG.info("Found SCM information: " + scmInfo);
        return Optional.of(scmInfo);
      }
    } catch (Exception e) {
      LOG.info("Failed to extract MANIFEST from jar: ", e);
      return Optional.absent();
    }
    return Optional.absent();
  }

  public static String getLaunchJarName() {
    URL url = getLaunchJarURL();
    if (url != null) {
      return FilenameUtils.getName(url.getPath());
    } else {
      return null;
    }
  }

  public static URL getLaunchJarURL() {

    try {
      //  if there's a jobjar on the path, assume we launched with it
      for (URL url : getClasspathJars()) {
        String name = FilenameUtils.getName(url.getPath());
        if (JOB_JAR_MATCHER.matcher(name).matches()) {
          LOG.info("Found launch jar from classpath jars: " + url);
          return url;
        }
      }
      return JarUtils.getMainJarURL();
    } catch (Exception e) {
      LOG.error("Failed to get launch dir URL: ", e);
      return null;
    }
  }

  public static void main(String[] args) {
    System.out.println(getLaunchJarName());
  }


}
