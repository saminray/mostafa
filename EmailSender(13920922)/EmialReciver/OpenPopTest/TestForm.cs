using System;
using System.Collections.Generic;
using System.Data;
using System.IO;
using System.Text;
using System.Windows.Forms;
using OpenPop.Mime;
using OpenPop.Mime.Header;
using OpenPop.Pop3;
using OpenPop.Pop3.Exceptions;
using OpenPop.Common.Logging;
using Message = OpenPop.Mime.Message;
using System.Threading;
using MailInbox.DAL;
using MailInbox.DataModel;


namespace OpenPop.TestApplication
{
    /// <summary>
    /// This class is a form which makes it possible to download all messages
    /// from a pop3 mailbox in a simply way.
    /// </summary>
    public class TestForm : Form
    {       
        private ThreadStart trs;
        private Thread tr;
        private readonly Dictionary<int, Message> messages = new Dictionary<int, Message>();
        private Pop3Client pop3Client;
        private Button connectAndRetrieveButton;
        private Button uidlButton;
        private Panel attachmentPanel;
        private ContextMenu contextMenuMessages;
        private DataGrid gridHeaders;
        private Label labelServerAddress;
        private Label labelServerPort;
        private Label labelAttachments;
        private Label labelMessageBody;
        private Label labelMessageNumber;
        private Label labelTotalMessages;
        private Label labelPassword;
        private Label labelUsername;
        private TreeView listAttachments;
        private TreeView listMessages;
        private MenuItem menuDeleteMessage;
        private MenuItem menuViewSource;
        private Panel panelTop;
        private Panel panelProperties;
        private Panel panelMiddle;
        private Panel panelMessageBody;
        private Panel panelMessagesView;
        private SaveFileDialog saveFile;
        private TextBox loginTextBox;
        private TextBox messageTextBox;
        private TextBox popServerTextBox;
        private TextBox passwordTextBox;
        private TextBox portTextBox;
        private TextBox totalMessagesTextBox;
        private ProgressBar progressBar;
        private System.Windows.Forms.Timer tmrMailChecker;
        private System.ComponentModel.IContainer components;
        private CheckBox useSslCheckBox;
        string EmailUsername = "";
        string EmailPassword = "";
        string MailServer = "";
        System.Threading.Timer CheckTEmailRecivetimer;
        public TestForm(string pUsername,string pPassword,string pMailserver)
        {
            EmailUsername = pUsername;
            EmailPassword = pPassword;
            MailServer = pMailserver;          
            InitializeComponent();            
            DefaultLogger.SetLog(new FileLogger());           
            FileLogger.Enabled = true;
            FileLogger.Verbose = true;
            tmrMailChecker.Enabled = true;
            pop3Client = new Pop3Client();          
            string myDocs = Environment.GetFolderPath(Environment.SpecialFolder.Personal);
            string file = Path.Combine(myDocs, "OpenPopLogin.txt");
            if (File.Exists(file))
            {
                using (StreamReader reader = new StreamReader(File.OpenRead(file)))
                {
                    popServerTextBox.Text = reader.ReadLine(); // Hostname
                    portTextBox.Text = reader.ReadLine(); // Port
                    useSslCheckBox.Checked = bool.Parse(reader.ReadLine() ?? "true"); // Whether to use SSL or not
                    loginTextBox.Text = reader.ReadLine(); // Username
                    passwordTextBox.Text = reader.ReadLine(); // Password
                }
            }
            tmrMailChecker.Enabled = true;
             CheckTEmailRecivetimer = new System.Threading.Timer(this.CheckTEmailRecivetimerCheck, this, TimeSpan.FromSeconds(1), TimeSpan.FromSeconds(60));   
        }
        struct MessageUID
        {
            public int Index;
            public  string UID;
        }
        #region Windows Form Designer generated code
        /// <summary>
        ///   Required method for Designer support - do not modify
        ///   the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(TestForm));
            this.panelTop = new System.Windows.Forms.Panel();
            this.useSslCheckBox = new System.Windows.Forms.CheckBox();
            this.uidlButton = new System.Windows.Forms.Button();
            this.totalMessagesTextBox = new System.Windows.Forms.TextBox();
            this.labelTotalMessages = new System.Windows.Forms.Label();
            this.labelPassword = new System.Windows.Forms.Label();
            this.passwordTextBox = new System.Windows.Forms.TextBox();
            this.labelUsername = new System.Windows.Forms.Label();
            this.loginTextBox = new System.Windows.Forms.TextBox();
            this.connectAndRetrieveButton = new System.Windows.Forms.Button();
            this.labelServerPort = new System.Windows.Forms.Label();
            this.portTextBox = new System.Windows.Forms.TextBox();
            this.labelServerAddress = new System.Windows.Forms.Label();
            this.popServerTextBox = new System.Windows.Forms.TextBox();
            this.panelProperties = new System.Windows.Forms.Panel();
            this.gridHeaders = new System.Windows.Forms.DataGrid();
            this.panelMiddle = new System.Windows.Forms.Panel();
            this.panelMessageBody = new System.Windows.Forms.Panel();
            this.progressBar = new System.Windows.Forms.ProgressBar();
            this.messageTextBox = new System.Windows.Forms.TextBox();
            this.labelMessageBody = new System.Windows.Forms.Label();
            this.panelMessagesView = new System.Windows.Forms.Panel();
            this.listMessages = new System.Windows.Forms.TreeView();
            this.contextMenuMessages = new System.Windows.Forms.ContextMenu();
            this.menuDeleteMessage = new System.Windows.Forms.MenuItem();
            this.menuViewSource = new System.Windows.Forms.MenuItem();
            this.labelMessageNumber = new System.Windows.Forms.Label();
            this.attachmentPanel = new System.Windows.Forms.Panel();
            this.listAttachments = new System.Windows.Forms.TreeView();
            this.labelAttachments = new System.Windows.Forms.Label();
            this.saveFile = new System.Windows.Forms.SaveFileDialog();
            this.tmrMailChecker = new System.Windows.Forms.Timer(this.components);
            this.panelTop.SuspendLayout();
            this.panelProperties.SuspendLayout();
            ((System.ComponentModel.ISupportInitialize)(this.gridHeaders)).BeginInit();
            this.panelMiddle.SuspendLayout();
            this.panelMessageBody.SuspendLayout();
            this.panelMessagesView.SuspendLayout();
            this.attachmentPanel.SuspendLayout();
            this.SuspendLayout();
            // 
            // panelTop
            // 
            this.panelTop.Controls.Add(this.useSslCheckBox);
            this.panelTop.Controls.Add(this.uidlButton);
            this.panelTop.Controls.Add(this.totalMessagesTextBox);
            this.panelTop.Controls.Add(this.labelTotalMessages);
            this.panelTop.Controls.Add(this.labelPassword);
            this.panelTop.Controls.Add(this.passwordTextBox);
            this.panelTop.Controls.Add(this.labelUsername);
            this.panelTop.Controls.Add(this.loginTextBox);
            this.panelTop.Controls.Add(this.connectAndRetrieveButton);
            this.panelTop.Controls.Add(this.labelServerPort);
            this.panelTop.Controls.Add(this.portTextBox);
            this.panelTop.Controls.Add(this.labelServerAddress);
            this.panelTop.Controls.Add(this.popServerTextBox);
            this.panelTop.Dock = System.Windows.Forms.DockStyle.Top;
            this.panelTop.Location = new System.Drawing.Point(0, 0);
            this.panelTop.Name = "panelTop";
            this.panelTop.Size = new System.Drawing.Size(865, 64);
            this.panelTop.TabIndex = 0;
            // 
            // useSslCheckBox
            // 
            this.useSslCheckBox.AutoSize = true;
            this.useSslCheckBox.Checked = true;
            this.useSslCheckBox.CheckState = System.Windows.Forms.CheckState.Checked;
            this.useSslCheckBox.Location = new System.Drawing.Point(19, 38);
            this.useSslCheckBox.Name = "useSslCheckBox";
            this.useSslCheckBox.Size = new System.Drawing.Size(68, 17);
            this.useSslCheckBox.TabIndex = 4;
            this.useSslCheckBox.Text = "Use SSL";
            this.useSslCheckBox.UseVisualStyleBackColor = true;
            // 
            //// uidlButton
            //// 
            //this.uidlButton.Enabled = false;
            //this.uidlButton.Location = new System.Drawing.Point(460, 42);
            //this.uidlButton.Name = "uidlButton";
            //this.uidlButton.Size = new System.Drawing.Size(82, 21);
            //this.uidlButton.TabIndex = 6;
            //this.uidlButton.Text = "UIDL";
            //this.uidlButton.Click += new System.EventHandler(this.UidlButtonClick);
            // 
            // totalMessagesTextBox
            // 
            this.totalMessagesTextBox.Location = new System.Drawing.Point(553, 30);
            this.totalMessagesTextBox.Name = "totalMessagesTextBox";
            this.totalMessagesTextBox.Size = new System.Drawing.Size(100, 20);
            this.totalMessagesTextBox.TabIndex = 7;
            // 
            // labelTotalMessages
            // 
            this.labelTotalMessages.Location = new System.Drawing.Point(553, 7);
            this.labelTotalMessages.Name = "labelTotalMessages";
            this.labelTotalMessages.Size = new System.Drawing.Size(100, 23);
            this.labelTotalMessages.TabIndex = 9;
            this.labelTotalMessages.Text = "Total Messages";
            // 
            // labelPassword
            // 
            this.labelPassword.Location = new System.Drawing.Point(264, 36);
            this.labelPassword.Name = "labelPassword";
            this.labelPassword.Size = new System.Drawing.Size(64, 23);
            this.labelPassword.TabIndex = 8;
            this.labelPassword.Text = "Password";
            // 
            // passwordTextBox
            // 
            this.passwordTextBox.Location = new System.Drawing.Point(328, 36);
            this.passwordTextBox.Name = "passwordTextBox";
            this.passwordTextBox.PasswordChar = '*';
            this.passwordTextBox.Size = new System.Drawing.Size(128, 20);
            this.passwordTextBox.TabIndex = 2;
            // 
            // labelUsername
            // 
            this.labelUsername.Location = new System.Drawing.Point(264, 5);
            this.labelUsername.Name = "labelUsername";
            this.labelUsername.Size = new System.Drawing.Size(64, 23);
            this.labelUsername.TabIndex = 6;
            this.labelUsername.Text = "Username";
            // 
            // loginTextBox
            // 
            this.loginTextBox.Location = new System.Drawing.Point(328, 5);
            this.loginTextBox.Name = "loginTextBox";
            this.loginTextBox.Size = new System.Drawing.Size(128, 20);
            this.loginTextBox.TabIndex = 1;
            // 
            // connectAndRetrieveButton
            // 
            this.connectAndRetrieveButton.Location = new System.Drawing.Point(460, 0);
            this.connectAndRetrieveButton.Name = "connectAndRetrieveButton";
            this.connectAndRetrieveButton.Size = new System.Drawing.Size(82, 39);
            this.connectAndRetrieveButton.TabIndex = 5;
            this.connectAndRetrieveButton.Text = "Connect and Retreive";
            this.connectAndRetrieveButton.Click += new System.EventHandler(this.ConnectAndRetrieveButtonClick);
            // 
            // labelServerPort
            // 
            this.labelServerPort.Location = new System.Drawing.Point(97, 39);
            this.labelServerPort.Name = "labelServerPort";
            this.labelServerPort.Size = new System.Drawing.Size(31, 23);
            this.labelServerPort.TabIndex = 3;
            this.labelServerPort.Text = "Port";
            // 
            // portTextBox
            // 
            this.portTextBox.Location = new System.Drawing.Point(128, 39);
            this.portTextBox.Name = "portTextBox";
            this.portTextBox.Size = new System.Drawing.Size(128, 20);
            this.portTextBox.TabIndex = 3;
            this.portTextBox.Text = "110";
            // 
            // labelServerAddress
            // 
            this.labelServerAddress.Location = new System.Drawing.Point(16, 8);
            this.labelServerAddress.Name = "labelServerAddress";
            this.labelServerAddress.Size = new System.Drawing.Size(112, 23);
            this.labelServerAddress.TabIndex = 1;
            this.labelServerAddress.Text = "POP Server Address";
            // 
            // popServerTextBox
            // 
            this.popServerTextBox.Location = new System.Drawing.Point(128, 8);
            this.popServerTextBox.Name = "popServerTextBox";
            this.popServerTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Horizontal;
            this.popServerTextBox.Size = new System.Drawing.Size(128, 20);
            this.popServerTextBox.TabIndex = 0;
            // 
            // panelProperties
            // 
            this.panelProperties.Controls.Add(this.gridHeaders);
            this.panelProperties.Dock = System.Windows.Forms.DockStyle.Bottom;
            this.panelProperties.Location = new System.Drawing.Point(0, 260);
            this.panelProperties.Name = "panelProperties";
            this.panelProperties.Size = new System.Drawing.Size(865, 184);
            this.panelProperties.TabIndex = 1;
            // 
            // gridHeaders
            // 
            this.gridHeaders.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.gridHeaders.DataMember = "";
            this.gridHeaders.HeaderForeColor = System.Drawing.SystemColors.ControlText;
            this.gridHeaders.Location = new System.Drawing.Point(0, 0);
            this.gridHeaders.Name = "gridHeaders";
            this.gridHeaders.PreferredColumnWidth = 400;
            this.gridHeaders.ReadOnly = true;
            this.gridHeaders.Size = new System.Drawing.Size(865, 188);
            this.gridHeaders.TabIndex = 3;
            // 
            // panelMiddle
            // 
            this.panelMiddle.Controls.Add(this.panelMessageBody);
            this.panelMiddle.Controls.Add(this.panelMessagesView);
            this.panelMiddle.Controls.Add(this.attachmentPanel);
            this.panelMiddle.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panelMiddle.Location = new System.Drawing.Point(0, 64);
            this.panelMiddle.Name = "panelMiddle";
            this.panelMiddle.Size = new System.Drawing.Size(865, 196);
            this.panelMiddle.TabIndex = 2;
            // 
            // panelMessageBody
            // 
            this.panelMessageBody.Controls.Add(this.progressBar);
            this.panelMessageBody.Controls.Add(this.messageTextBox);
            this.panelMessageBody.Controls.Add(this.labelMessageBody);
            this.panelMessageBody.Dock = System.Windows.Forms.DockStyle.Fill;
            this.panelMessageBody.Location = new System.Drawing.Point(281, 0);
            this.panelMessageBody.Name = "panelMessageBody";
            this.panelMessageBody.Size = new System.Drawing.Size(376, 196);
            this.panelMessageBody.TabIndex = 6;
            // 
            // progressBar
            // 
            this.progressBar.Anchor = ((System.Windows.Forms.AnchorStyles)(((System.Windows.Forms.AnchorStyles.Bottom | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.progressBar.Location = new System.Drawing.Point(7, 172);
            this.progressBar.Name = "progressBar";
            this.progressBar.Size = new System.Drawing.Size(360, 12);
            this.progressBar.TabIndex = 10;
            // 
            // messageTextBox
            // 
            this.messageTextBox.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.messageTextBox.Location = new System.Drawing.Point(7, 22);
            this.messageTextBox.MaxLength = 999999999;
            this.messageTextBox.Multiline = true;
            this.messageTextBox.Name = "messageTextBox";
            this.messageTextBox.ScrollBars = System.Windows.Forms.ScrollBars.Both;
            this.messageTextBox.Size = new System.Drawing.Size(360, 143);
            this.messageTextBox.TabIndex = 9;
            // 
            // labelMessageBody
            // 
            this.labelMessageBody.Location = new System.Drawing.Point(8, 8);
            this.labelMessageBody.Name = "labelMessageBody";
            this.labelMessageBody.Size = new System.Drawing.Size(136, 16);
            this.labelMessageBody.TabIndex = 5;
            this.labelMessageBody.Text = "Message Body";
            // 
            // panelMessagesView
            // 
            this.panelMessagesView.Controls.Add(this.listMessages);
            this.panelMessagesView.Controls.Add(this.labelMessageNumber);
            this.panelMessagesView.Dock = System.Windows.Forms.DockStyle.Left;
            this.panelMessagesView.Location = new System.Drawing.Point(0, 0);
            this.panelMessagesView.Name = "panelMessagesView";
            this.panelMessagesView.Size = new System.Drawing.Size(281, 196);
            this.panelMessagesView.TabIndex = 5;
            // 
            // listMessages
            // 
            this.listMessages.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.listMessages.ContextMenu = this.contextMenuMessages;
            this.listMessages.Location = new System.Drawing.Point(8, 24);
            this.listMessages.Name = "listMessages";
            this.listMessages.Size = new System.Drawing.Size(266, 160);
            this.listMessages.TabIndex = 8;
            this.listMessages.AfterSelect += new System.Windows.Forms.TreeViewEventHandler(this.ListMessagesMessageSelected);
            // 
            // contextMenuMessages
            // 
            this.contextMenuMessages.MenuItems.AddRange(new System.Windows.Forms.MenuItem[] {
            this.menuDeleteMessage,
            this.menuViewSource});
            // 
            // menuDeleteMessage
            // 
            this.menuDeleteMessage.Index = 0;
            this.menuDeleteMessage.Text = "Delete Mail";
            this.menuDeleteMessage.Click += new System.EventHandler(this.MenuDeleteMessageClick);
            // 
            //// menuViewSource
            //// 
            //this.menuViewSource.Index = 1;
            //this.menuViewSource.Text = "View source";
            //this.menuViewSource.Click += new System.EventHandler(this.MenuViewSourceClick);
            // 
            // labelMessageNumber
            // 
            this.labelMessageNumber.Location = new System.Drawing.Point(8, 8);
            this.labelMessageNumber.Name = "labelMessageNumber";
            this.labelMessageNumber.Size = new System.Drawing.Size(136, 16);
            this.labelMessageNumber.TabIndex = 1;
            this.labelMessageNumber.Text = "Messages";
            // 
            // attachmentPanel
            // 
            this.attachmentPanel.Controls.Add(this.listAttachments);
            this.attachmentPanel.Controls.Add(this.labelAttachments);
            this.attachmentPanel.Dock = System.Windows.Forms.DockStyle.Right;
            this.attachmentPanel.Location = new System.Drawing.Point(657, 0);
            this.attachmentPanel.Name = "attachmentPanel";
            this.attachmentPanel.Size = new System.Drawing.Size(208, 196);
            this.attachmentPanel.TabIndex = 4;
            this.attachmentPanel.Visible = false;
            // 
            // listAttachments
            // 
            this.listAttachments.Anchor = ((System.Windows.Forms.AnchorStyles)((((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Bottom)
                        | System.Windows.Forms.AnchorStyles.Left)
                        | System.Windows.Forms.AnchorStyles.Right)));
            this.listAttachments.Location = new System.Drawing.Point(8, 24);
            this.listAttachments.Name = "listAttachments";
            this.listAttachments.ShowLines = false;
            this.listAttachments.ShowRootLines = false;
            this.listAttachments.Size = new System.Drawing.Size(192, 160);
            this.listAttachments.TabIndex = 10;
            this.listAttachments.AfterSelect += new System.Windows.Forms.TreeViewEventHandler(this.ListAttachmentsAttachmentSelected);
            // 
            // labelAttachments
            // 
            this.labelAttachments.Location = new System.Drawing.Point(12, 8);
            this.labelAttachments.Name = "labelAttachments";
            this.labelAttachments.Size = new System.Drawing.Size(136, 16);
            this.labelAttachments.TabIndex = 3;
            this.labelAttachments.Text = "Attachments";
            // 
            // saveFile
            // 
            this.saveFile.Title = "Save Attachment";
            // 
            // tmrMailChecker
            // 
            //this.tmrMailChecker.Enabled = true;
            //this.tmrMailChecker.Interval = 2000;
            //this.tmrMailChecker.Tick += new System.EventHandler(this.tmrMailChecker_Tick);
            // 
            // TestForm
            // 
            this.AutoScaleBaseSize = new System.Drawing.Size(5, 13);
            this.ClientSize = new System.Drawing.Size(865, 444);
            this.Controls.Add(this.panelMiddle);
            this.Controls.Add(this.panelProperties);
            this.Controls.Add(this.panelTop);
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "TestForm";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "OpenPOP.NET Test Application";
            this.WindowState = System.Windows.Forms.FormWindowState.Maximized;
            this.Load += new System.EventHandler(this.TestForm_Load);
            this.panelTop.ResumeLayout(false);
            this.panelTop.PerformLayout();
            this.panelProperties.ResumeLayout(false);
            ((System.ComponentModel.ISupportInitialize)(this.gridHeaders)).EndInit();
            this.panelMiddle.ResumeLayout(false);
            this.panelMessageBody.ResumeLayout(false);
            this.panelMessageBody.PerformLayout();
            this.panelMessagesView.ResumeLayout(false);
            this.attachmentPanel.ResumeLayout(false);
            this.ResumeLayout(false);

        }
        #endregion
    
        [STAThread]
        private static void Main()
        {
            //Application.Run(new TestForm());
        }
       
        int lastCount;
        public struct messageBody
        {
            public short TransactionCountRequested;
            public string DepositeNubmer;
            public string MailSender;
            public bool Blocked;
            public messageBody(short pTrans, string pAcountNumber,string pMailSender)
            {
                TransactionCountRequested = pTrans;
                DepositeNubmer = pAcountNumber;
                MailSender = pMailSender;
                Blocked = false;
            }
            public messageBody(short pTrans, string pAcountNumber, string pMailSender,bool pBlocked)
            {
                TransactionCountRequested = pTrans;
                DepositeNubmer = pAcountNumber;
                MailSender = pMailSender;
                Blocked = pBlocked;
            }
        }
        public static bool EmailReciveCheckerISbusy = false;
        public static bool EmailAddCheckerISbusy = false;
        public static List<messageBody> messageBodyList = new List<messageBody>();
        List<string> listOfMessage;
        private bool IsExist(string UID)
        { 
            try
            {
                MailInboxModel pmibmodel = new MailInboxModel();
                pmibmodel.ID = null;
                pmibmodel.MessageHeader = null;
                pmibmodel.MessageBody = null;
                pmibmodel.MessageUID =UID;
                pmibmodel.Read = null;
                pmibmodel.receiveDate = null;
                pmibmodel.receiveTime = null;
                pmibmodel.SenderMail = null;
            MailInboxDO mibdo = new MailInboxDO();
            List<MailInboxModel> mibmodel = new  List<MailInboxModel>();
            mibmodel = mibdo.Search(pmibmodel);
            if (mibmodel .Count!=0)
                return true;
            else
                return false;
            }
            catch (Exception ex)
            {
                return false;
            }
        }

        public bool Ifmorethan9(Message message)
        {
            List< MailInboxModel> mailInboxmodelList=new List<MailInboxModel>();
           MailInboxDO mailInboxdo=new MailInboxDO ();
            mailInboxmodelList=mailInboxdo.Search(new MailInboxModel{receiveDate=message.Headers.DateSent.ToShortDateString(),SenderMail=message.Headers.From.Address});
            if (mailInboxmodelList.Count <= 12)
                return false;
            else return true;
			 
			
        }
        public List<messageBody> ReceiveMails()
        {

            int count = 0;
            MailInboxDO miDo = new MailInboxDO();
            MailInboxModel messageModel = new MailInboxModel();
            connectAndRetrieveButton.Enabled = false;
            uidlButton.Enabled = false;
            progressBar.Value = 0;
            int success = 0;
            try
            {
                if (pop3Client.Connected)
                {
                    pop3Client.Disconnect();
                }
                pop3Client.Connect(MailServer, 110, false);
                pop3Client.Authenticate(EmailUsername, EmailPassword);
                count = pop3Client.GetMessageCount();
                int fail = 0;
                List<MessageUID> listcountUid = new List<MessageUID>();
                List<string> lmsUid = pop3Client.GetMessageUids();
                //اگر ایمیل دریافتی از قبل خوانده نشده بود و Delivery
                //نبود وارد لیست ایمیل ها نشود
                
                for (int i = lmsUid.Count-1; i >= 0; i--)
                {
                    if (!IsExist(lmsUid[i]))
                    {
                        Message msg = pop3Client.GetMessage(i+1);
                        if ( msg.Headers.From.Address!="Mailer-Daemon@server.mypowerwebhost.com" && !Ifmorethan9(msg))
                        {
                      // pop3Client.GetMessageHeaders(i).Sender.Address != "Mailer-Daemon@server.mypowerwebhost.com")
                       
                            MessageUID messageUid = new MessageUID();
                            messageUid.Index = i+1;
                            messageUid.UID = lmsUid[i];
                            listcountUid.Add(messageUid);
                        }
                        else
                        {
                            try
                            {
                                MailInboxModel messageModel1 = new MailInboxModel();
                                messageModel.MessageHeader = msg.Headers.Subject;                               
                                messageModel.MessageBody = "";
                                messageModel1.MessageUID = lmsUid[i];
                                messageModel1.receiveDate = msg.Headers.DateSent.ToShortDateString();
                                messageModel1.receiveTime = msg.Headers.DateSent.ToShortTimeString();
                                messageModel1.SenderMail = msg.Headers.From.Address;
                                messageModel1.Read = false;
                                miDo.Insert(messageModel1, true);
                            }
                            catch (Exception ex)
                            {
                                LogRegister("ReceiveMails_DatabaseInsert", ex.Message);
                            }
                        }
                    }
                }
                foreach (MessageUID msgUID in listcountUid)
                {
                    if (IsDisposed)
                        return null;
                    Application.DoEvents();
                    try
                    {
                        string msUid = pop3Client.GetMessageUid(msgUID.Index);
                        Message message = pop3Client.GetMessage(msgUID.Index);
                        if (!messages.ContainsKey(msgUID.Index))
                        {
                            messages.Add(msgUID.Index, message);    
                        }                        
                        MessagePart msgpart = message.FindFirstPlainTextVersion();
                        short trans; string AcountNumber;
                        string[] messageBody;
                        if (msgpart != null)
                            messageBody = msgpart.GetBodyAsText().Split(':');
                        else
                            messageBody = new string[1];
                        try
                        {

                            AcountNumber = messageBody[0];
                            trans = short.Parse(messageBody[1].Trim());

                            //اگر این شماره حساب در این روز کمتر از 10 بار درخواست داده می تواند مجدد درخواست دهد
                            List<MailInboxModel> mailInboxmodelList = new List<MailInboxModel>();
                            MailInboxDO mailInboxdo = new MailInboxDO();
                            mailInboxmodelList = mailInboxdo.Search(new MailInboxModel { receiveDate = message.Headers.DateSent.ToShortDateString() });
                            int countInaDay = 0;
                            foreach (MailInboxModel tmpMail in mailInboxmodelList)
                            {
                                if (tmpMail.MessageBody != null && tmpMail.MessageBody.Contains(AcountNumber))
                                {
                                    countInaDay++;
                                }
                            }
                            if (countInaDay <= 9)
                            {
                                while (true)
                                {
                                    if (!EmailReciveCheckerISbusy && !EmailAddCheckerISbusy)
                                    {
                                        EmailAddCheckerISbusy = true;
                                        messageBodyList.Add(new messageBody(trans, AcountNumber, message.Headers.From.ToString()));
                                        EmailAddCheckerISbusy = false;
                                        break;
                                    }
                                }
                            }
                            else if(countInaDay<=12)
                            {
                                while (true)
                                {
                                    if (!EmailReciveCheckerISbusy && !EmailAddCheckerISbusy)
                                    {
                                        EmailAddCheckerISbusy = true;
                                        messageBodyList.Add(new messageBody(trans, AcountNumber, message.Headers.From.ToString(),true));
                                        EmailAddCheckerISbusy = false;
                                        break;
                                    }
                                }
                            }
                        }
                      
                        catch (IndexOutOfRangeException exp)
                        {
                            while (true)
                            {
                                if (!EmailReciveCheckerISbusy)
                                {
                                    EmailAddCheckerISbusy = true;
                                    if (!message.Headers.From.ToString().Contains("info@omidejelin.ir"))
                                    {
                                        messageBodyList.Add(new messageBody(0, "0", message.Headers.From.ToString()));
                                    }
                                    EmailAddCheckerISbusy = false;
                                    break;
                                }
                            }
                        }
                        catch (Exception ex)
                        {
                            while (true)
                            {
                                if (!EmailReciveCheckerISbusy)
                                {
                                    EmailAddCheckerISbusy = true;
                                    if (!message.Headers.From.ToString().Contains("info@omidejelin.ir"))
                                    {
                                        messageBodyList.Add(new messageBody(0, "0", message.Headers.From.ToString()));
                                    }
                                    EmailAddCheckerISbusy = false;
                                    break;
                                }
                            }
                        }
                        try
                        {
                            messageModel = new MailInboxModel();
                            messageModel.MessageHeader = message.Headers.Subject;
                            if (msgpart != null)
                                messageModel.MessageBody = msgpart.GetBodyAsText().Replace("\r\n", string.Empty);
                            else
                                messageModel.MessageBody = "ERROR TO read Body";
                            messageModel.MessageUID = msUid;
                            messageModel.receiveDate = message.Headers.DateSent.ToShortDateString();
                            messageModel.receiveTime = message.Headers.DateSent.ToShortTimeString();
                            messageModel.SenderMail = message.Headers.From.Address;
                            messageModel.Read = false;
                            miDo.Insert(messageModel, true);
                        }
                        catch (Exception ex) 
                        {
                            LogRegister("ReceiveMails_DatabaseInsert", ex.Message);
                        }

                    }
                    catch (Exception e)
                    {
                        LogRegister("ReceiveMails", e.Message + "\r\n" +"Stack trace:\r\n" +e.StackTrace);                        
                    }
                }                
            }
            //catch (InvalidLoginException)
            //{
               
            //    //MessageBox.Show(this, "The server did not accept the user credentials!", "POP3 Server Authentication");
            //}
            //catch (PopServerNotFoundException)
            //{
            //    //MessageBox.Show(this, "The server could not be found", "POP3 Retrieval");
            //}
            //catch (PopServerLockedException)
            //{
            //    // MessageBox.Show(this, "The mailbox is locked. It might be in use or under maintenance. Are you connected elsewhere?", "POP3 Account Locked");
            //}
            //catch (LoginDelayException)
            //{
            //    //MessageBox.Show(this, "Login not allowed. Server enforces delay between logins. Have you connected recently?", "POP3 Account Login Delay");
            //}
            catch (Exception e)
            {
                LogRegister("ReceiveMails", e.Message);  
                //MessageBox.Show(this, "Error occurred retrieving mail. " + e.Message, "POP3 Retrieval");
            }
            finally
            {
                Isbusy = false;
            }
            return messageBodyList;
        }

        public void DeleteMail(int ID)
        {
            messageBodyList.RemoveAt(ID);
        }

        private void ConnectAndRetrieveButtonClick(object sender, EventArgs e)
        {
            ReceiveMails();
        }

        private void ListMessagesMessageSelected(object sender, TreeViewEventArgs e)
        {
            try
            {
                // Fetch out the selected message
                Message message = messages[GetMessageNumberFromSelectedNode(listMessages.SelectedNode)];

                // If the selected node contains a MessagePart and we can display the contents - display them
                if (listMessages.SelectedNode.Tag is MessagePart)
                {
                    MessagePart selectedMessagePart = (MessagePart)listMessages.SelectedNode.Tag;
                    if (selectedMessagePart.IsText)
                    {
                        // We can show text MessageParts
                        messageTextBox.Text = selectedMessagePart.GetBodyAsText();
                    }
                    else
                    {
                        // We are not able to show non-text MessageParts (MultiPart messages, images, pdf's ...)
                        messageTextBox.Text = "<<OpenPop>> Cannot show this part of the email. It is not text <<OpenPop>>";
                    }
                }
                else
                {
                    // If the selected node is not a subnode and therefore does not
                    // have a MessagePart in it's Tag property, we genericly find some content to show

                    // Find the first text/plain version
                    MessagePart plainTextPart = message.FindFirstPlainTextVersion();
                    if (plainTextPart != null)
                    {
                        // The message had a text/plain version - show that one
                        messageTextBox.Text = plainTextPart.GetBodyAsText();
                    }
                    else
                    {
                        // Try to find a body to show in some of the other text versions
                        List<MessagePart> textVersions = message.FindAllTextVersions();
                        if (textVersions.Count >= 1)
                            messageTextBox.Text = textVersions[0].GetBodyAsText();
                        else
                            messageTextBox.Text = "<<OpenPop>> Cannot find a text version body in this message to show <<OpenPop>>";
                    }
                }

                // Clear the attachment list from any previus shown attachments
                listAttachments.Nodes.Clear();

                // Build up the attachment list
                List<MessagePart> attachments = message.FindAllAttachments();
                foreach (MessagePart attachment in attachments)
                {
                    // Add the attachment to the list of attachments
                    TreeNode addedNode = listAttachments.Nodes.Add((attachment.FileName));

                    // Keep a reference to the attachment in the Tag property
                    addedNode.Tag = attachment;
                }

                // Only show that attachmentPanel if there is attachments in the message
                bool hadAttachments = attachments.Count > 0;
                attachmentPanel.Visible = hadAttachments;

                // Generate header table
                DataSet dataSet = new DataSet();
                DataTable table = dataSet.Tables.Add("Headers");
                table.Columns.Add("Header");
                table.Columns.Add("Value");

                DataRowCollection rows = table.Rows;

                // Add all known headers
                rows.Add(new object[] { "Content-Description", message.Headers.ContentDescription });
                rows.Add(new object[] { "Content-Id", message.Headers.ContentId });
                foreach (string keyword in message.Headers.Keywords) rows.Add(new object[] { "Keyword", keyword });
                foreach (RfcMailAddress dispositionNotificationTo in message.Headers.DispositionNotificationTo) rows.Add(new object[] { "Disposition-Notification-To", dispositionNotificationTo });
                foreach (Received received in message.Headers.Received) rows.Add(new object[] { "Received", received.Raw });
                rows.Add(new object[] { "Importance", message.Headers.Importance });
                rows.Add(new object[] { "Content-Transfer-Encoding", message.Headers.ContentTransferEncoding });
                foreach (RfcMailAddress cc in message.Headers.Cc) rows.Add(new object[] { "Cc", cc });
                foreach (RfcMailAddress bcc in message.Headers.Bcc) rows.Add(new object[] { "Bcc", bcc });
                foreach (RfcMailAddress to in message.Headers.To) rows.Add(new object[] { "To", to });
                rows.Add(new object[] { "From", message.Headers.From });
                rows.Add(new object[] { "Reply-To", message.Headers.ReplyTo });
                foreach (string inReplyTo in message.Headers.InReplyTo) rows.Add(new object[] { "In-Reply-To", inReplyTo });
                foreach (string reference in message.Headers.References) rows.Add(new object[] { "References", reference });
                rows.Add(new object[] { "Sender", message.Headers.Sender });
                rows.Add(new object[] { "Content-Type", message.Headers.ContentType });
                rows.Add(new object[] { "Content-Disposition", message.Headers.ContentDisposition });
                rows.Add(new object[] { "Date", message.Headers.Date });
                rows.Add(new object[] { "Date", message.Headers.DateSent });
                rows.Add(new object[] { "Message-Id", message.Headers.MessageId });
                rows.Add(new object[] { "Mime-Version", message.Headers.MimeVersion });
                rows.Add(new object[] { "Return-Path", message.Headers.ReturnPath });
                rows.Add(new object[] { "Subject", message.Headers.Subject });

                // Add all unknown headers
                foreach (string key in message.Headers.UnknownHeaders)
                {
                    string[] values = message.Headers.UnknownHeaders.GetValues(key);
                    if (values != null)
                        foreach (string value in values)
                        {
                            rows.Add(new object[] { key, value });
                        }
                }

                // Now set the headers displayed on the GUI to the header table we just generated
                gridHeaders.DataMember = table.TableName;
                gridHeaders.DataSource = dataSet;
            }
            catch (Exception exp) { }
        }

        /// <summary>
        /// Finds the MessageNumber of a Message given a <see cref="TreeNode"/> to search in.
        /// The root of this <see cref="TreeNode"/> should have the Tag property set to a int, which
        /// points into the <see cref="messages"/> dictionary.
        /// </summary>
        /// <param name="node">The <see cref="TreeNode"/> to look in. Cannot be <see langword="null"/>.</param>
        /// <returns>The found int</returns>
        private static int GetMessageNumberFromSelectedNode(TreeNode node)
        {
            if (node == null)
                throw new ArgumentNullException("node");

            // Check if we are at the root, by seeing if it has the Tag property set to an int
            if (node.Tag is int)
            {
                return (int)node.Tag;
            }

            // Otherwise we are not at the root, move up the tree
            return GetMessageNumberFromSelectedNode(node.Parent);
        }

        private void ListAttachmentsAttachmentSelected(object sender, TreeViewEventArgs args)
        {
            // Fetch the attachment part which is currently selected
            MessagePart attachment = (MessagePart)listAttachments.SelectedNode.Tag;

            //if (attachment != null)
            //{
            //    saveFile.FileName = attachment.FileName;
            //    DialogResult result = saveFile.ShowDialog();
            //    if (result != DialogResult.OK)
            //        return;

            //    // Now we want to save the attachment
            //    FileInfo file = new FileInfo(saveFile.FileName);

            //    // Check if the file already exists
            //    if(file.Exists)
            //    {
            //        // User was asked when he chose the file, if he wanted to overwrite it
            //        // Therefore, when we get to here, it is okay to delete the file
            //        file.Delete();
            //    }

            //    // Lets try to save to the file
            //    try
            //    {
            //        attachment.Save(file);

            //        MessageBox.Show(this, "Attachment saved successfully");
            //    } catch (Exception e)
            //    {
            //        MessageBox.Show(this, "Attachment saving failed. Exception message: " + e.Message);
            //    }
            //}
            //else
            //{
            //    MessageBox.Show(this, "Attachment object was null!");
            //}
        }

        private void MenuDeleteMessageClick(object sender, EventArgs e)
        {
            //if (listMessages.SelectedNode != null)
            //{
            //    DialogResult drRet = MessageBox.Show(this, "Are you sure to delete the email?", "Delete email", MessageBoxButtons.YesNo);
            //    if (drRet == DialogResult.Yes)
            //    {
            //        int messageNumber = GetMessageNumberFromSelectedNode(listMessages.SelectedNode);
            //        pop3Client.DeleteMessage(messageNumber);
            //        //messageNumber
            //        listMessages.Nodes[listMessages.SelectedNode.Index].Remove();

            //        //drRet = MessageBox.Show(this, "Do you want to receive email again (this will commit your changes)?", "Receive email", MessageBoxButtons.YesNo);
            //        //if (drRet == DialogResult.Yes)
            //            ReceiveMails();
            //    }
            //}
        }
        List<string> uids = new List<string>();
        bool firsLoad;
        private void TestForm_Load(object sender, EventArgs e)
        {
            firsLoad = true;
            tmrMailChecker.Enabled = true; 
            this.Hide();                     
        }
        
        void CheckTEmailRecivetimerCheck(object stat)
        {
            if (!Isbusy)
            {
                Isbusy = true;
                ReceiveMails();
            }
        }
        bool Isbusy;

        public static void LogRegister(string Source, string Message)
        {
            try
            {
                System.Diagnostics.EventLog.WriteEntry(Source, Message);
                Console.WriteLine(Source + " : " + Message);
            }
            catch (Exception)
            {

            }

        }
       
    }
}