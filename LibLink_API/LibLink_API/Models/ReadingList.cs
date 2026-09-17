namespace LibLink_API.Models
{
    public class ReadingList
    {
        public int ReadingListID { get; set; }

        public int UserID { get; set; }

        public int BookID { get; set; }

        public DateTime AddedDate { get; set; } = DateTime.UtcNow;

        public User User { get; set; } = null!;

        public Book Book { get; set; } = null!;
    }
}
