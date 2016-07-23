using Microsoft.Owin;
using Owin;

[assembly: OwinStartupAttribute(typeof(ExpatMig.Startup))]
namespace ExpatMig
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            ConfigureAuth(app);
        }
    }
}
