namespace LibLink_API.Models
{
    public class Reservation
    {
        public int ReservationID { get; set; }

        public int UserID { get; set; }

        public int BookID { get; set; }

        public DateTime ReservationDate { get; set; }

        public string Status { get; set; } = "Waiting";

        public User User { get; set; } = null!;

        public Book Book { get; set; } = null!;
    }
}
