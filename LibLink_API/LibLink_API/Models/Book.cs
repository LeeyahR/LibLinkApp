using Microsoft.AspNetCore.Mvc.ViewEngines;

namespace LibLink_API.Models
{
    public class Book
    {
        public int BookID { get; set; }

        public string Title { get; set; } = string.Empty;

        public string Author { get; set; } = string.Empty;

        public string ISBN { get; set; } = string.Empty;

        public string Genre { get; set; } = string.Empty;

        public string Description { get; set; } = string.Empty;

        public string? CoverImage { get; set; }

        public int TotalCopies { get; set; }

        public int AvailableCopies { get; set; }

        public ICollection<Borrowing> Borrowings { get; set; }
            = new List<Borrowing>();

        public ICollection<Reservation> Reservations { get; set; }
            = new List<Reservation>();

        public ICollection<Review> Reviews { get; set; }
            = new List<Review>();

        public ICollection<ReadingList> ReadingLists { get; set; }
            = new List<ReadingList>();
    }
}
