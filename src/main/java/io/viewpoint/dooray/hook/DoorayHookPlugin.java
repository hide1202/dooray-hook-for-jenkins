package io.viewpoint.dooray.hook;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.AbstractProject;
import hudson.model.Run;
import hudson.model.TaskListener;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;

// TODO if this class writes Kotlin, cannot find Descriptor
public class DoorayHookPlugin extends Builder implements SimpleBuildStep {
    private String url;
    private String botName;
    private String text;
    private String botIconImage;

    @DataBoundConstructor
    public DoorayHookPlugin() {
    }

    // config.jelly fetch saved data from getter methods. So MUST be getter methods.
    public String getUrl() {
        return url;
    }

    @DataBoundSetter
    public void setUrl(String url) {
        this.url = url;
    }

    public String getBotName() {
        return botName;
    }

    @DataBoundSetter
    public void setBotName(String botName) {
        this.botName = botName;
    }

    public String getText() {
        return text;
    }

    @DataBoundSetter
    public void setText(String text) {
        this.text = text;
    }

    public String getBotIconImage() {
        return botIconImage;
    }

    @DataBoundSetter
    public void setBotIconImage(String botIconImage) {
        this.botIconImage = botIconImage;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher, @Nonnull TaskListener listener) {
        HookService hookService = new HookService(listener);
        String botIconImage = StringUtils.isEmpty(this.botIconImage) ? null : this.botIconImage;
        hookService.hook(url, new Hook(botName, botIconImage, text, null));
    }

    @Override
    public DoorayHookPluginDescriptor getDescriptor() {
        return (DoorayHookPluginDescriptor) super.getDescriptor();
    }

    @Extension
    public static class DoorayHookPluginDescriptor extends BuildStepDescriptor<Builder> {
        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }

        @Nonnull
        @Override
        public String getDisplayName() {
            return "Dooray Hook";
        }
    }
}

