package com.example.expense_tracker.controller;

import com.example.expense_tracker.model.*;
import com.example.expense_tracker.repo.*;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class TransactionController {

    private final TransactionRepository txRepo;
    private final BalanceRepository balRepo;
    private final UserRepository userRepo;

    public TransactionController(TransactionRepository txRepo,
                                 BalanceRepository balRepo,
                                 UserRepository userRepo) {
        this.txRepo   = txRepo;
        this.balRepo  = balRepo;
        this.userRepo = userRepo;
    }

    // ═══════════════════════════════════════════════════════
    //  AUTH  — /api/login  &  /api/signup
    // ═══════════════════════════════════════════════════════

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody User req) {
        User user = userRepo.findByUsernameAndPassword(req.getUsername(), req.getPassword());
        Map<String, Object> res = new HashMap<>();
        if (user != null) {
            res.put("success", true);
            res.put("userId",    user.getId());
            res.put("username",  user.getUsername());
            res.put("firstName", user.getFirstName() != null ? user.getFirstName() : user.getUsername());
            res.put("lastName",  user.getLastName()  != null ? user.getLastName()  : "");
            res.put("email",     user.getEmail()     != null ? user.getEmail()     : "");
        } else {
            res.put("success", false);
            res.put("message", "Invalid username or password");
        }
        return res;
    }

    @PostMapping("/signup")
    public Map<String, Object> signup(@RequestBody User req) {
        Map<String, Object> res = new HashMap<>();
        if (userRepo.findByUsername(req.getUsername()) != null) {
            res.put("success", false);
            res.put("message", "Username already taken");
            return res;
        }
        User saved = userRepo.save(req);
        // create a zero balance row for the new user
        Balance b = new Balance();
        b.setUserId(saved.getId());
        b.setTotalBalance(0.0);
        balRepo.save(b);

        res.put("success",   true);
        res.put("userId",    saved.getId());
        res.put("username",  saved.getUsername());
        res.put("firstName", saved.getFirstName() != null ? saved.getFirstName() : saved.getUsername());
        res.put("lastName",  saved.getLastName()  != null ? saved.getLastName()  : "");
        res.put("email",     saved.getEmail()     != null ? saved.getEmail()     : "");
        return res;
    }

    // ═══════════════════════════════════════════════════════
    //  EXPENSES  — CRUD
    // ═══════════════════════════════════════════════════════

    @GetMapping("/expenses/{userId}")
    public ApiResponse<List<Transaction>> getExpenses(@PathVariable Long userId) {
        return new ApiResponse<>(true, txRepo.findByUserIdOrderByDateDesc(userId));
    }

    @PostMapping("/expenses")
    public ApiResponse<Transaction> addExpense(@RequestBody Transaction t) {
        if (t.getDate() == null) t.setDate(java.time.LocalDate.now());
        Transaction saved = txRepo.save(t);

        // Deduct from balance
        Balance bal = balRepo.findByUserId(t.getUserId());
        if (bal == null) {
            bal = new Balance();
            bal.setUserId(t.getUserId());
            bal.setTotalBalance(-t.getAmount());
        } else {
            bal.setTotalBalance(bal.getTotalBalance() - t.getAmount());
        }
        balRepo.save(bal);

        return new ApiResponse<>(true, saved);
    }

    @DeleteMapping("/expenses/{id}")
    public ApiResponse<String> deleteExpense(@PathVariable Long id,
                                              @RequestParam Long userId) {
        txRepo.findById(id).ifPresent(t -> {
            // Restore balance when expense deleted
            Balance bal = balRepo.findByUserId(userId);
            if (bal != null) {
                bal.setTotalBalance(bal.getTotalBalance() + t.getAmount());
                balRepo.save(bal);
            }
            txRepo.deleteById(id);
        });
        return new ApiResponse<>(true, "Deleted");
    }

    // ═══════════════════════════════════════════════════════
    //  BALANCE  — add / get
    // ═══════════════════════════════════════════════════════

    @GetMapping("/balance/{userId}")
    public ApiResponse<Double> getBalance(@PathVariable Long userId) {
        Balance bal = balRepo.findByUserId(userId);
        return new ApiResponse<>(true, bal != null ? bal.getTotalBalance() : 0.0);
    }

    @PostMapping("/balance")
    public ApiResponse<Balance> addBalance(@RequestBody Balance req) {
        Balance bal = balRepo.findByUserId(req.getUserId());
        if (bal == null) {
            bal = new Balance();
            bal.setUserId(req.getUserId());
            bal.setTotalBalance(req.getTotalBalance());
        } else {
            bal.setTotalBalance(bal.getTotalBalance() + req.getTotalBalance());
        }
        return new ApiResponse<>(true, balRepo.save(bal));
    }

    // ═══════════════════════════════════════════════════════
    //  STATS  — totals + category breakdown
    // ═══════════════════════════════════════════════════════

    @GetMapping("/stats/{userId}")
    public Map<String, Object> stats(@PathVariable Long userId) {
        List<Transaction> list = txRepo.findByUserIdOrderByDateDesc(userId);

        double totalExpenses = list.stream().mapToDouble(Transaction::getAmount).sum();

        Map<String, Double> catTotals = new LinkedHashMap<>();
        for (Transaction t : list) {
            catTotals.merge(t.getCategory(), t.getAmount(), Double::sum);
        }

        Balance bal = balRepo.findByUserId(userId);
        double balance = (bal != null) ? bal.getTotalBalance() : 0.0;

        Map<String, Object> res = new HashMap<>();
        res.put("totalExpenses",   totalExpenses);
        res.put("totalBalance",    balance);
        res.put("categoryTotals",  catTotals);
        return res;
    }

    // ═══════════════════════════════════════════════════════
    //  PROFILE  — update
    // ═══════════════════════════════════════════════════════

    @PutMapping("/profile/{userId}")
    public Map<String, Object> updateProfile(@PathVariable Long userId,
                                              @RequestBody User req) {
        Map<String, Object> res = new HashMap<>();
        Optional<User> opt = userRepo.findById(userId);
        if (opt.isEmpty()) { res.put("success", false); return res; }
        User user = opt.get();
        if (req.getFirstName() != null) user.setFirstName(req.getFirstName());
        if (req.getLastName()  != null) user.setLastName(req.getLastName());
        if (req.getEmail()     != null) user.setEmail(req.getEmail());
        userRepo.save(user);
        res.put("success",   true);
        res.put("firstName", user.getFirstName());
        res.put("lastName",  user.getLastName());
        res.put("email",     user.getEmail());
        return res;
    }
}
