using Microsoft.EntityFrameworkCore;

public class LibLink_APIContext(DbContextOptions<LibLink_APIContext> options) : DbContext(options)
{
    public DbSet<LibLink_API.Models.User> User { get; set; } = default!;
    public DbSet<LibLink_API.Models.Book> Book { get; set; } = default!;
    public DbSet<LibLink_API.Models.Borrowing> Borrowing { get; set; } = default!;
    public DbSet<LibLink_API.Models.ReadingList> ReadingList { get; set; } = default!;
    public DbSet<LibLink_API.Models.Reservation> Reservation { get; set; } = default!;
    public DbSet<LibLink_API.Models.Review> Review { get; set; } = default!;
}
