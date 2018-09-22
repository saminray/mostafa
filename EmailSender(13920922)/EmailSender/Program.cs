using System;
using System.Collections.Generic;
using System.Linq;
using System.Windows.Forms;

namespace EmailSender
{

    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            Console.Write("program Started ....");
            //string user = Console.ReadLine();
            //string Password = Console.ReadLine();
            string user ="2227514" ;
            string Password ="qaz~!@#$123" ;
            Console.Clear();
            EmailBankCore EB = new EmailBankCore(user,Password);
            if (!EB.Status)
            {
                Application.Exit();
                Console.WriteLine("Faild To login Please Restart Program And Enter A USername Or Password Carefuly");
                Console.Read();
            }
            else
            {
                Console.WriteLine("Email Sending......");
                Console.Read();
            }
        }
    }
}
