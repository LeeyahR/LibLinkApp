using Microsoft.EntityFrameworkCore;

public partial class Program
{
    private static void Main(string[] args)
    {
        var builder = WebApplication.CreateBuilder(args);

        var connectionString =
            builder.Configuration.GetConnectionString("LibLink_APIContext")
            ?? throw new InvalidOperationException(
                "Connection string 'LibLink_APIContext' not found.");

        builder.Services.AddDbContext<LibLink_APIContext>(options =>
            options.UseSqlServer(connectionString));

        builder.Services.AddControllers();

        builder.Services.AddEndpointsApiExplorer();
        builder.Services.AddSwaggerGen();

        var app = builder.Build();

        // Enable Swagger in Azure as well as during local development
        app.UseSwagger();
        app.UseSwaggerUI();

        app.UseAuthorization();

        app.MapControllers();

        app.Run();
    }
}