using Microsoft.AspNetCore.Mvc.ViewEngines;

namespace LibLink_API.Models
{
    public class User
    {
        public int UserID { get; set; }

        public string FullName { get; set; } = string.Empty;

        public string Email { get; set; } = string.Empty;

        public string StudentNumber { get; set; } = string.Empty;

        public string PasswordHash { get; set; } = string.Empty;

        public string Role { get; set; } = "Student";

        public DateTime CreatedDate { get; set; } = DateTime.UtcNow;

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
