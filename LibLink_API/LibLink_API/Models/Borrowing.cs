namespace LibLink_API.Models
{
    public class Borrowing
    {
        public int BorrowingID { get; set; }

        public int UserID { get; set; }

        public int BookID { get; set; }

        public DateTime BorrowedDate { get; set; }

        public DateTime DueDate { get; set; }

        public DateTime? ReturnedDate { get; set; }

        public string Status { get; set; } = "Borrowed";

        public User User { get; set; } = null!;

        public Book Book { get; set; } = null!;
    }
}
