package org.jenkinsci.plugins;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import hudson.util.FormValidation;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.ServletException;

import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;
import org.kohsuke.stapler.StaplerRequest;
import net.sf.json.JSONObject;

public class IFTTTBuildNotifier extends Notifier {

    private final static Logger LOG = Logger.getLogger(IFTTTBuildNotifier.class.getName());

    private final String key;
    private final String eventName;

    @DataBoundConstructor
    public IFTTTBuildNotifier(String key, String eventName) {
        this.key = key;
        this.eventName = eventName;
    }

    public String getKey() {
        return key;
    }

    public String getEventName() {
        return eventName;
    }

    public BuildStepMonitor getRequiredMonitorService() {
        return BuildStepMonitor.NONE;
    }

    @Override
    public boolean perform(AbstractBuild<?, ?> build, Launcher launcher,
                           BuildListener listener) throws InterruptedException, IOException {

        String buildResult = build.getResult().toString();
        int buildNumber = build.getNumber();
        String projectName = build.getProject().getName();

        try {

            Client client = Client.create();

            WebResource webResource = client
                    .resource("http://maker.ifttt.com/trigger/" + getEventName() + "/with/key/" + getKey());

            JSONObject json = new JSONObject();
            json.put("value1", projectName);
            json.put("value2", buildNumber);
            json.put("value3", buildResult);

            ClientResponse response = webResource.type("application/json")
                    .post(ClientResponse.class, json.toString());

            if (response.getStatus() != 200) {
                throw new RuntimeException("Failed to call IFTTT Build Trigger - HTTP error code : "
                        + response.getStatus());
            }
        } catch (Exception e) {
            listener.error("Failed to call IFTTT Trigger...");
            listener.error(e.toString());
        }
        return true;
    }

    @Override
    public boolean needsToRunAfterFinalized() {
        return true;
    }

    @Override
    public DescriptorImpl getDescriptor() {
        return (DescriptorImpl) super.getDescriptor();
    }

    @Extension
    public static final class DescriptorImpl extends
            BuildStepDescriptor<Publisher> {

        public DescriptorImpl() {
            super(IFTTTBuildNotifier.class);
            load();
        }

        @Override
        public String getDisplayName() {
            return "IFTTT Build Notifier";
        }

        public FormValidation doCheckKey(
                @QueryParameter("key") final String key)
                throws IOException, ServletException {

            if (key.length() == 0)
                return FormValidation.error("Please set the Key");

            return FormValidation.ok();
        }

        public FormValidation doCheckEventName(
                @QueryParameter("eventName") final String key)
                throws IOException, ServletException {

            if (key.length() == 0)
                return FormValidation.error("Please set the Event Name");

            return FormValidation.ok();
        }

        @Override
        public Publisher newInstance(StaplerRequest req, JSONObject formData)
                throws FormException {
            return super.newInstance(req, formData);
        }

        @Override
        public boolean isApplicable(Class<? extends AbstractProject> jobType) {
            return true;
        }
    }
}
