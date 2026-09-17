
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using LibLink_API.Models;

namespace LibLink_API.Controllers;

[ApiController]
[Route("api/reservations")]
public class ReservationsController : ControllerBase
{
    private readonly LibLink_APIContext _context;

    public ReservationsController(LibLink_APIContext context)
    {
        _context = context;
    }

    [HttpPost]
    public async Task<IActionResult> Reserve(int userId, int bookId)
    {
        var book = await _context.Book.FindAsync(bookId);

        if (book == null)
            return NotFound("Book not found.");

        if (book.AvailableCopies > 0)
            return BadRequest(
                "This book is currently available. You can borrow it.");

        var reservation = new Reservation
        {
            UserID = userId,
            BookID = bookId,
            ReservationDate = DateTime.UtcNow,
            Status = "Waiting"
        };

        _context.Reservation.Add(reservation);

        await _context.SaveChangesAsync();

        return Ok(reservation);
    }

    [HttpGet("user/{userId}")]
    public async Task<IActionResult> GetReservations(int userId)
    {
        var reservations =
            await _context.Reservation
                .Include(x => x.Book)
                .Where(x => x.UserID == userId)
                .ToListAsync();

        return Ok(reservations);
    }
}