package io.viewpoint.dooray.hook;

import hudson.Extension;
import hudson.FilePath;
import hudson.Launcher;
import hudson.model.*;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Builder;
import jenkins.tasks.SimpleBuildStep;
import org.apache.commons.lang3.StringUtils;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.stream.Collectors;

// TODO if this class writes Kotlin, cannot find Descriptor
public class DoorayHookPlugin extends Builder implements SimpleBuildStep {
    private String url;
    private String botName;
    private String text;
    private String botIconImage;
    private List<Entry> attachments;

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

    public List<Entry> getAttachments() {
        return attachments;
    }

    @DataBoundSetter
    public void setAttachments(List<Entry> attachments) {
        this.attachments = attachments;
    }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher, @Nonnull TaskListener listener) {
        HookService hookService = new HookService(listener);
        String botIconImage = StringUtils.isEmpty(this.botIconImage) ? null : this.botIconImage;
        List<Attachment> attachments = this.attachments.stream().filter(e -> e instanceof AttachmentEntry)
                .map(e -> (AttachmentEntry) e)
                .map(at -> {
                    String title = at.getTitle();
                    String titleLink = StringUtils.isEmpty(at.getTitleLink()) ? null : at.getTitleLink();
                    String text = at.getText();
                    String color = StringUtils.isEmpty(at.getColor()) ? null : at.getColor();
                    String image = StringUtils.isEmpty(at.getImage()) ? null : at.getImage();
                    return new Attachment(title, titleLink, text, color, image);
                }).collect(Collectors.toList());
        hookService.hook(url, new Hook(botName, botIconImage, text, attachments));
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


    public static abstract class Entry extends AbstractDescribableImpl<Entry> {
    }

    public static final class AttachmentEntry extends Entry {
        private String title;
        private String titleLink;
        private String text;
        private String color;
        private String image;

        @DataBoundConstructor
        public AttachmentEntry() {
        }

        public String getTitle() {
            return title;
        }

        @DataBoundSetter
        public void setTitle(String title) {
            this.title = title;
        }

        public String getTitleLink() {
            return titleLink;
        }

        @DataBoundSetter
        public void setTitleLink(String titleLink) {
            this.titleLink = titleLink;
        }

        public String getText() {
            return text;
        }

        @DataBoundSetter
        public void setText(String text) {
            this.text = text;
        }

        public String getColor() {
            return color;
        }

        @DataBoundSetter
        public void setColor(String color) {
            this.color = color;
        }

        public String getImage() {
            return image;
        }

        @DataBoundSetter
        public void setImage(String image) {
            this.image = image;
        }

        @Extension
        public static class DescriptorImpl extends Descriptor<Entry> {
            @Override
            public String getDisplayName() {
                return "Attachment";
            }
        }
    }

}

