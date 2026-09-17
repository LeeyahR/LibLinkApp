
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using LibLink_API.Models;

namespace LibLink_API.Controllers;

[ApiController]
[Route("api/readingLists")]
public class ReadingListsController : ControllerBase
{
    private readonly LibLink_APIContext _context;

    public ReadingListsController(LibLink_APIContext context)
    {
        _context = context;
    }

    [HttpPost]
    public async Task<IActionResult> Add(ReadingList item)
    {
        _context.ReadingList.Add(item);

        await _context.SaveChangesAsync();

        return Ok(item);
    }

    [HttpGet("user/{userId}")]
    public async Task<IActionResult> GetUserList(int userId)
    {
        var list = await _context.ReadingList
            .Include(x => x.Book)
            .Where(x => x.UserID == userId)
            .ToListAsync();

        return Ok(list);
    }
}
