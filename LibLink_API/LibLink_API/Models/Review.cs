namespace LibLink_API.Models
{
    public class Review
    {
        public int ReviewID { get; set; }

        public int UserID { get; set; }

        public int BookID { get; set; }

        public int Rating { get; set; }

        public string Comment { get; set; } = string.Empty;

        public DateTime CreatedDate { get; set; } = DateTime.UtcNow;

        public User User { get; set; } = null!;

        public Book Book { get; set; } = null!;
    }
}
