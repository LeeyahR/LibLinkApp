
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using LibLink_API.Models;

namespace LibLink_API.Controllers;

[ApiController]
[Route("api/borrowings")]
public class BorrowingsController : ControllerBase
{
    private readonly LibLink_APIContext _context;

    public BorrowingsController(LibLink_APIContext context)
    {
        _context = context;
    }

    [HttpPost]
    public async Task<IActionResult> Borrow(int userId, int bookId)
    {
        var book = await _context.Book.FindAsync(bookId);

        if (book == null)
            return NotFound("Book not found.");

        if (book.AvailableCopies <= 0)
            return BadRequest("This book is currently unavailable.");

        var borrowing = new Borrowing
        {
            UserID = userId,
            BookID = bookId,
            BorrowedDate = DateTime.UtcNow,
            DueDate = DateTime.UtcNow.AddDays(14),
            Status = "Borrowed"
        };

        book.AvailableCopies--;

        _context.Borrowing.Add(borrowing);

        await _context.SaveChangesAsync();

        return Ok(borrowing);
    }

    [HttpPut("{id}/return")]
    public async Task<IActionResult> ReturnBook(int id)
    {
        var borrowing =
            await _context.Borrowing
                .Include(x => x.Book)
                .FirstOrDefaultAsync(x => x.BorrowingID == id);

        if (borrowing == null)
            return NotFound();

        if (borrowing.Status == "Returned")
            return BadRequest("Book has already been returned.");

        borrowing.Status = "Returned";
        borrowing.ReturnedDate = DateTime.UtcNow;

        borrowing.Book.AvailableCopies++;

        await _context.SaveChangesAsync();

        return Ok(borrowing);
    }

    [HttpGet("user/{userId}")]
    public async Task<IActionResult> GetUserBorrowings(int userId)
    {
        var borrowings =
            await _context.Borrowing
                .Include(x => x.Book)
                .Where(x => x.UserID == userId)
                .OrderByDescending(x => x.BorrowedDate)
                .ToListAsync();

        return Ok(borrowings);
    }
}

