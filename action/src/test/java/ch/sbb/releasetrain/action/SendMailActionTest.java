package ch.sbb.releasetrain.action;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import ch.sbb.releasetrain.config.model.email.MailReceiver;
import ch.sbb.releasetrain.config.releasecalendar.ConfigAccessorImpl;
import ch.sbb.releasetrain.state.model.ActionResult;
import ch.sbb.releasetrain.state.model.ActionState;
import ch.sbb.releasetrain.utils.emails.SMTPUtilImpl;
import ch.sbb.releasetrain.utils.yaml.YamlModelAccessor;

import com.dumbster.smtp.SimpleSmtpServer;

@RunWith(MockitoJUnitRunner.class)
public class SendMailActionTest {

    @Mock
    private ConfigAccessorImpl config;

    @Mock
    private MailReceiver mailRec;

    private SimpleSmtpServer server;
    private SendMailAction action;
    private ActionState mailState;

    @org.junit.Before
    public void setUp() throws Exception {

        String in = IOUtils.toString(getClass().getResourceAsStream("/test-mail-action-state.yaml"));
        YamlModelAccessor<ActionState> yaml = new YamlModelAccessor<>();
        mailState = yaml.convertEntry(in);
        server = SimpleSmtpServer.start(2525);

        SMTPUtilImpl smtp = new SMTPUtilImpl();
        smtp.setMailhost("localhost");
        smtp.setMailport(2525);

        action = new SendMailAction();
        action.setConfig(config);
        action.setSmtpUtil(smtp);

        action.setConfig(config);

        List<MailReceiver> receiverList = new ArrayList<>();

        when(mailRec.getEmail()).thenReturn("bla@bla.bl");
        receiverList.add(mailRec);
        when(config.readMailReveiverForMailinglist(anyString())).thenReturn(receiverList);

    }

    @org.junit.After
    public void tearDown() throws Exception {
        server.stop();
    }

    @org.junit.Test
    public void testGetName() throws Exception {
        assertEquals("emailAction", action.getName());
    }

    @org.junit.Test
    public void testDoWork() throws Exception {
        ActionResult result = action.doWork(mailState, "releaseVersion", "snapshotVersion", "maintenanceVersion");
        assertNotNull(result);
        assertNotNull(result == ActionResult.SUCCESS);
        assertEquals(1, server.getReceivedEmailSize());
    }
}