
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using LibLink_API.Models;

namespace LibLink_API.Controllers;

[ApiController]
[Route("api/reviews")]
public class ReviewsController : ControllerBase
{
    private readonly LibLink_APIContext _context;

    public ReviewsController(LibLink_APIContext context)
    {
        _context = context;
    }

    [HttpGet("book/{bookId}")]
    public async Task<IActionResult> GetReviews(int bookId)
    {
        var reviews = await _context.Review
            .Include(x => x.User)
            .Where(x => x.BookID == bookId)
            .ToListAsync();

        return Ok(reviews);
    }

    [HttpPost]
    public async Task<IActionResult> AddReview(Review review)
    {
        if (review.Rating < 1 || review.Rating > 5)
            return BadRequest("Rating must be between 1 and 5.");

        _context.Review.Add(review);

        await _context.SaveChangesAsync();

        return Ok(review);
    }
}
