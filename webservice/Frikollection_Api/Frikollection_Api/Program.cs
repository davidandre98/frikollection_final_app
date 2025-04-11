using Frikollection_Api.Infraestructure;
using Frikollection_Api.Models;
using Frikollection_Api.Services;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.FileProviders;

var builder = WebApplication.CreateBuilder(args);

// Add services to the container.

builder.Services.AddControllers();
// Learn more about configuring Swagger/OpenAPI at https://aka.ms/aspnetcore/swashbuckle
builder.Services.AddEndpointsApiExplorer();
builder.Services.AddSwaggerGen();

builder.Services.AddDbContext<FrikollectionContext>(options =>
    options.UseSqlServer(builder.Configuration.GetConnectionString("DefaultConnection")));

builder.Services.AddScoped<IUserService, UserService>();
builder.Services.AddScoped<ICollectionService, CollectionService>();
builder.Services.AddScoped<IProductService, ProductService>();
builder.Services.AddScoped<IProductExtensionService, ProductExtensionService>();
builder.Services.AddScoped<IProductTypeService, ProductTypeService>();
builder.Services.AddScoped<ITagService, TagService>();
builder.Services.AddScoped<IPasswordHasher<User>, PasswordHasher<User>>();

builder.Services.AddHttpContextAccessor();

var app = builder.Build();

// Configure the HTTP request pipeline.
if (app.Environment.IsDevelopment())
{
    app.UseSwagger();
    app.UseSwaggerUI();
}

app.UseHttpsRedirection();

// Serveix wwwroot (per defecte)
app.UseStaticFiles();

// Serveix wwwroot/uploads/ com a /images/uploads/
app.UseStaticFiles(new StaticFileOptions
{
    RequestPath = "/images/uploads",
    FileProvider = new PhysicalFileProvider(
        Path.Combine(builder.Environment.WebRootPath ?? "wwwroot", "uploads"))
});

app.UseAuthorization();

app.MapControllers();

app.Run();
