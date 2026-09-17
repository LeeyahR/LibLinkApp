
using Microsoft.AspNetCore.Mvc;
using Microsoft.EntityFrameworkCore;
using LibLink_API.Models;

namespace LibLink_API.Controllers;

[ApiController]
[Route("api/books")]
public class BooksController : ControllerBase
{
    private readonly LibLink_APIContext _context;

    public BooksController(LibLink_APIContext context)
    {
        _context = context;
    }

    [HttpGet]
    public async Task<IActionResult> GetBooks()
    {
        var books = await _context.Book
            .OrderBy(x => x.Title)
            .ToListAsync();

        return Ok(books);
    }

    [HttpGet("{id}")]
    public async Task<IActionResult> GetBook(int id)
    {
        var book = await _context.Book
            .FirstOrDefaultAsync(x => x.BookID == id);

        if (book == null)
            return NotFound("Book not found.");

        return Ok(book);
    }

    [HttpGet("search")]
    public async Task<IActionResult> Search(
        [FromQuery] string search)
    {
        if (string.IsNullOrWhiteSpace(search))
            return await GetBooks();

        var books = await _context.Book
            .Where(x =>
                x.Title.Contains(search) ||
                x.Author.Contains(search) ||
                x.Genre.Contains(search) ||
                x.ISBN.Contains(search))
            .ToListAsync();

        return Ok(books);
    }

    [HttpPost]
    public async Task<IActionResult> AddBook(Book book)
    {
        if (string.IsNullOrWhiteSpace(book.Title))
            return BadRequest("Book title is required.");

        _context.Book.Add(book);

        await _context.SaveChangesAsync();

        return CreatedAtAction(
            nameof(GetBook),
            new { id = book.BookID },
            book);
    }

    [HttpPut("{id}")]
    public async Task<IActionResult> UpdateBook(int id, Book updatedBook)
    {
        var book = await _context.Book.FindAsync(id);

        if (book == null)
            return NotFound();

        book.Title = updatedBook.Title;
        book.Author = updatedBook.Author;
        book.ISBN = updatedBook.ISBN;
        book.Genre = updatedBook.Genre;
        book.Description = updatedBook.Description;
        book.TotalCopies = updatedBook.TotalCopies;
        book.AvailableCopies = updatedBook.AvailableCopies;

        await _context.SaveChangesAsync();

        return Ok(book);
    }

    [HttpDelete("{id}")]
    public async Task<IActionResult> DeleteBook(int id)
    {
        var book = await _context.Book.FindAsync(id);

        if (book == null)
            return NotFound();

        _context.Book.Remove(book);

        await _context.SaveChangesAsync();

        return Ok("Book deleted.");
    }
}
